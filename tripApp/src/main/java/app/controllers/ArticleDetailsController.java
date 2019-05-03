package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
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
		model.addAttribute("loggedUserId", user.getId());
		
		session.setAttribute("articleId", articleId);
		
		return "views/all/articleDetailsView";
	}
	
	@PostMapping("/article")
	public String articleActions(Model model, HttpSession session, Principal principal,
								@RequestParam(name="commentContent", required = false) String commentContent,
								@RequestParam(name="submit", required = false) String actionType,
								@RequestParam MultiValueMap<String, String> selectedComment) {
			
		if(selectedComment != null) {
			for(Map.Entry<String, List<String>> commentId : selectedComment.entrySet()){
					if(commentId.getValue().contains("Sterge")){
						this.removeComment(Integer.parseInt(commentId.getKey()));
						break;
					}
			   }
		}

		if(actionType != null) {
			switch(actionType) {
				case "Adauga comentariu":{
					this.addCommentToArticle(commentContent, Integer.parseInt((String)session.getAttribute("articleId")), principal);
					break;
				}
				case "Reseteaza":{
					break;
				}
				case "Sterge":{
					System.out.println(selectedComment.toString());
					break;
				}
			}
		}
				
		String redirectUrl = "article?a=" + session.getAttribute("articleId");
		return "redirect:/" + redirectUrl;
	}
	
	private Article getArticleThatContainsComment(Integer commentId) {
		for(Article article : articleRepository.findAll()) {
			for(ArticleComment articleComment : article.getComments()) {
				if(articleComment.getCommentId() == commentId) {
					return article;
				}
			}
		}
		return null;
	}
	
	private void removeComment(Integer commentId) {
		Article article = this.getArticleThatContainsComment(commentId);
		List<ArticleComment> comments = article.getComments();
		
		for(ArticleComment comment : comments) {
			if(comment.getCommentId() == commentId) {
				comments.remove(comment);
				break;
			}
		}
		article.setComments(comments);
		articleRepository.save(article);
	}
	
	private void addCommentToArticle(String commentContent, Integer articleId, Principal principal) {
		UserEntity user = this.getUserByEmail(principal.getName());
		Article article = this.getArticleById(articleId);
		ArticleComment comment = new ArticleComment(this.getLastCommentId() + 1, user.getId(), LocalDate.now().toString(), commentContent);

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
		
		Collections.reverse(commentsDTO);
		
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
	
	private Integer getLastCommentId() {
		List<Integer> commentIds = new ArrayList<>();
		
		for(Article article : articleRepository.findAll()) {
			for(ArticleComment comment : article.getComments()) {
				if(!commentIds.contains(comment.getCommentId())) {
					commentIds.add(comment.getCommentId());
				}
			}
		}
		return this.getMaxIdFromList(commentIds);
	}
	
	private Integer getMaxIdFromList(List<Integer> commentIds) {
		if(commentIds.size() == 0) {
			return 0;
		}
		
		Integer max = commentIds.get(0);
		
		for(int index = 1; index < commentIds.size(); index++) {
			if(commentIds.get(index) > max) {
				max = commentIds.get(index);
			}
		}
		return max;
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
