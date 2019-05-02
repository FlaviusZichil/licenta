package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import app.models.ArticleDetailsViewModel;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;

@Controller
public class ArticleDetailsController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/article")
	public String getArticle(Model model, ArticleDetailsViewModel articleDetailsViewModel, HttpSession session, Principal principal,
						    @RequestParam(value = "a", required = false) String articleId) {

		Article article = this.getArticleById(Integer.parseInt(articleId));
		ArticleDTO articleDTO = this.convertFromArticleToArticleDTO(article);
		articleDetailsViewModel.setArticleDTO(articleDTO);		
		model.addAttribute("articleDetailsViewModel", articleDetailsViewModel);
		
		UserEntity user = this.getUserByEmail(principal.getName());
		model.addAttribute("userName", user.getLastName() + " " + user.getFirstName());	
		
		session.setAttribute("articleId", articleId);
		
		return "views/all/articleDetailsView";
	}
	
	@PostMapping("/article")
	public String articleActions(Model model, HttpSession session, Principal principal,
								@RequestParam(name="commentContent", required = false) String commentContent) {
		
		this.addCommentToArticle(commentContent, Integer.parseInt((String)session.getAttribute("articleId")), principal);
		
		String redirectUrl = "article?a=" + session.getAttribute("articleId");
		return "redirect:/" + redirectUrl;
	}
	
	private void addCommentToArticle(String commentContent, Integer articleId, Principal principal) {
		UserEntity user = this.getUserByEmail(principal.getName());
		Article article = this.getArticleById(articleId);
		ArticleComment comment = new ArticleComment(user.getId(), LocalDate.now().toString(), commentContent);

		List<ArticleComment> comments = article.getComments();		
		comments.add(comment);	
		articleRepository.save(article);	
	}
	
	private UserEntity getUserByEmail(String email) {	
		for(UserEntity user : userRepository.findAll()) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
	
	private Article getArticleById(Integer articleId) {
		for(Article article : articleRepository.findAll()) {
			if(article.getArticleId() == articleId) {
				return article;
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
		ArticleCommentDTO commentDTO = new ArticleCommentDTO(this.getUserById(comment.getUserId()), comment.getDate(), comment.getContent());
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
}
