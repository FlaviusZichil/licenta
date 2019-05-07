package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
import app.documents.ArticleComment;
import app.documents.ArticleLike;
import app.dto.ArticleCommentDTO;
import app.dto.ArticleDTO;
import app.dto.ArticleLikeDTO;
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
		
		model.addAttribute("allLikes", this.getAllLikesNumberForUser(user));
		model.addAttribute("allComments", this.getAllCommentsNumberForUser(user));
		
		return "views/staff/myArticlesView";
	}
	
	@PostMapping("/my-articles")
	public String myArticlesActions(Model model, Principal principal, HttpSession session,
								   @RequestParam MultiValueMap<String, String> articleToRemove) {
		UserEntity user = this.getUserByEmail(principal.getName());
		
		if(articleToRemove != null) {
			for(Map.Entry<String, List<String>> articleId : articleToRemove.entrySet()){
				if(articleId.getValue().contains("Sterge articolul")){
					this.removeArticleForUser(Integer.parseInt(articleId.getKey()), user);
					return "redirect:/my-articles";
				}
			}
			
			for(Map.Entry<String, List<String>> articleId : articleToRemove.entrySet()){
				if(articleId.getValue().contains("Modifica articolul")){
					session.setAttribute("isUserAllowedToEdit", true);
					String redirectUrl = "article?a=" + articleId.getKey();
					return "redirect:/" + redirectUrl;
				}
			}
		}
		
		return "views/staff/myArticlesView";
	}
	
	private Integer getAllLikesNumberForUser(UserEntity user) {
		Integer likes = 0;
		
		for(ArticleDTO article : this.getArticlesDTOForUser(user)) {
			if(article.getUser().getId() == user.getId()) {
				likes += article.getLikesDTO().size();
			}
		}
		return likes;
	}
	
	private Integer getAllCommentsNumberForUser(UserEntity user) {
		Integer comments = 0;
		
		for(ArticleDTO article : this.getArticlesDTOForUser(user)) {
			if(article.getUser().getId() == user.getId()) {
				comments += article.getCommentsDTO().size();
			}
		}
		return comments;
	}
	
	private void removeArticleForUser(Integer articleId, UserEntity user) {
		for(Article article : articleRepository.findAll()) {
			if(article.getArticleId() == articleId && article.getUserId() == user.getId()) {
				articleRepository.delete(article);
				System.out.println("article removed");
			}
		}
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
		List<ArticleLikeDTO> likesDTO = new ArrayList<>();
		
		for(ArticleComment comment : article.getComments()) {
			commentsDTO.add(this.convertFromArticleCommentToArticleCommentDTO(comment));
		}
		
		for(ArticleLike like : article.getLikes()) {
			likesDTO.add(this.convertFromArticleLikeToArticleLikeDTO(like));
		}
		
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), this.getUserById(article.getUserId()), article.getDate(), article.getTitle(), likesDTO, 
											   article.getDescription(), article.getSections(), commentsDTO);
		return articleDTO;
	}
	
	private ArticleLikeDTO convertFromArticleLikeToArticleLikeDTO(ArticleLike like) {
		ArticleLikeDTO articleLikeDTO = new ArticleLikeDTO(this.getUserById(like.getUserId()), this.getArticleById(like.getArticleId()));
		return articleLikeDTO;
	}
	
	private Article getArticleById(Integer articleId) {
		for(Article article : articleRepository.findAll()) {
			if(article.getArticleId() == articleId) {
				return article;
			}
		}
		return null;
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
