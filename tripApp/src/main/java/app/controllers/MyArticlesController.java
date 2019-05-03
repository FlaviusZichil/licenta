package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.documents.Article;
import app.documents.ArticleComment;
import app.dto.ArticleCommentDTO;
import app.dto.ArticleDTO;
import app.entities.UserEntity;
import app.models.AllArticlesViewModel;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;

@Controller
public class MyArticlesController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/my-articles")
	public String getMyArticles(Model model, Principal principal) {
		UserEntity user = this.getUserByEmail(principal.getName());
		
		AllArticlesViewModel myArticlesViewModel = new AllArticlesViewModel();
		myArticlesViewModel.setArticlesDTO(this.getArticlesDTOForUser(user));
		model.addAttribute("myArticlesViewModel", myArticlesViewModel);
		return "views/staff/myArticlesView";
	}
	
	@PostMapping("/my-articles")
	public String myArticlesActions(Model model) {
		return "views/staff/myArticlesView";
	}
	
	private List<ArticleDTO> getArticlesDTOForUser(UserEntity user){
		List<ArticleDTO> articlesDTOForUser = new ArrayList<>();
		
		for(Article article : articleRepository.findAll()) {
			if(article.getUserId() == user.getId()) {
				articlesDTOForUser.add(this.convertFromArticleToArticleDTO(article));
			}
		}
		return articlesDTOForUser;
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
	
	private UserEntity getUserById(Integer userId) {
		for(UserEntity user : userRepository.findAll()) {
			if(user.getId() == userId) {
				return user;
			}
		}
		return null;
	}
	
	private UserEntity getUserByEmail(String email) {		
		for(UserEntity user : userRepository.findAll()) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
