package app.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
import app.documents.ArticleComment;
import app.dto.ArticleCommentDTO;
import app.dto.ArticleDTO;
import app.entities.UserEntity;
import app.models.AllArticlesViewModel;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;

@Controller
public class AllArticlesController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/all-articles")
	public String getAllArticles(Model model) {
		AllArticlesViewModel allArticlesViewModel = new AllArticlesViewModel();
		allArticlesViewModel.setArticlesDTO(this.getAllArticlesDTO());
		model.addAttribute("allArticlesViewModel", allArticlesViewModel);
		return "views/all/allArticles";
	}
	
	@PostMapping("/all-articles")
	public String allArticlesActions(@RequestParam(name = "submit", required = false) String actionType) {
		switch(actionType) {
			case "Citeste mai mult":{
				return "redirect:/article";
			}
		}
		return "views/all/allArticles";
	}
	
	private List<ArticleDTO> getAllArticlesDTO(){
		List<ArticleDTO> articlesDTO = new ArrayList<>();
		for(Article article : articleRepository.findAll()) {
			articlesDTO.add(this.convertFromArticleToArticleDTO(article));
		}
		return articlesDTO;
	}
	
	private UserEntity getUserById(Integer userId) {
		for(UserEntity user : userRepository.findAll()) {
			if(user.getId() == userId) {
				return user;
			}
		}
		return null;
	}
	
	private ArticleDTO convertFromArticleToArticleDTO(Article article) {
		List<ArticleCommentDTO> commentsDTO = new ArrayList<>();
		
		for(ArticleComment comment : article.getComments()) {
			commentsDTO.add(this.convertFromArticleCommentToArticleCommentDTO(comment));
		}
		
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), this.getUserById(article.getUserId()), article.getDate(), article.getTitle(), article.getLikes(), 
											   article.getDescription(), article.getSections(), commentsDTO);
		return articleDTO;
	}
	
	private ArticleCommentDTO convertFromArticleCommentToArticleCommentDTO(ArticleComment comment) {
		LocalDate articleDate = LocalDate.parse(comment.getDate());
		Period period = Period.between(LocalDate.now(), articleDate);
		int days = Math.abs(period.getDays());
	    
		ArticleCommentDTO commentDTO = new ArticleCommentDTO(comment.getCommentId(), this.getUserById(comment.getUserId()), comment.getDate(), comment.getContent(), days);
		return commentDTO;
	}
}
