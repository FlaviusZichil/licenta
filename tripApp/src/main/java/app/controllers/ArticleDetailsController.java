package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
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
import app.documents.ArticleLike;
import app.documents.ArticleSection;
import app.dto.ArticleCommentDTO;
import app.dto.ArticleDTO;
import app.dto.ArticleLikeDTO;
import app.entities.UserEntity;
import app.models.ArticleDetailsViewModel;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;
import app.validators.ArticleValidator;

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
		model.addAttribute("isUserAllowedToEdit", false);
		
		if(this.isArticleAlreadyLikedByUser(article, user)) {
			model.addAttribute("alreadyLiked", true);
		}
		else {
			model.addAttribute("alreadyLiked", false);
		}
		
		this.verifyArticleComponent("wrongTitle", session, model);
		this.verifyArticleComponent("wrongDescription", session, model);
		this.verifyArticleComponent("wrongSectionTitle", session, model);
		this.verifyArticleComponent("wrongSectionContent", session, model);
		
		// pun pe sesiune in MyArticlesController
		if(session.getAttribute("isUserAllowedToEdit") != null) {
			if(((boolean)session.getAttribute("isUserAllowedToEdit")) == true) {
				model.addAttribute("isUserAllowedToEdit", true);
				session.setAttribute("isUserAllowedToEdit", null);
			}
		}

		session.setAttribute("articleId", articleId);
		
		return "views/all/articleDetailsView";
	}
	
	@PostMapping("/article")
	public String articleActions(Model model, HttpSession session, Principal principal,
								@RequestParam(name="commentContent", required = false) String commentContent,
								@RequestParam(name="submit", required = false) String actionType,
								@RequestParam(name = "title", required = false) String title,
								@RequestParam(name = "sectionTitles", required = false) String sectionsTitle,
								@RequestParam(name = "sectionContent", required = false) String sectionsContent,
								@RequestParam(name = "description", required = false) String description,
								@RequestParam MultiValueMap<String, String> submitInputs) {
			
		Article selectedArticle = this.getArticleById(Integer.parseInt((String) session.getAttribute("articleId")));
		UserEntity user = this.getUserByEmail(principal.getName());
		
		if(submitInputs != null) {
			for(Map.Entry<String, List<String>> commentId : submitInputs.entrySet()){
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
				case "Salveaza modificarile":{				
					if(this.isArticleValid(title, description, sectionsTitle, sectionsContent, session)) {
						this.saveArticleAfterEdit(selectedArticle, title, description, sectionsTitle, sectionsContent, session);
					}
					else {
						session.setAttribute("isUserAllowedToEdit", true);
					}
					break;
				}
				case "Apreciaza":{					
					addLikeForArticle(user, selectedArticle);
					break;
				}
				case "Nu mai aprecia":{
					removeLikeForArticle(user, selectedArticle);
					break;
				}
			}
		}				
		String redirectUrl = "article?a=" + session.getAttribute("articleId");
		return "redirect:/" + redirectUrl;
	}
	
	private boolean isArticleValid(String title, String description, String sectionsTitle, String sectionsContent, HttpSession session) {
		ArticleValidator validator = new ArticleValidator();
		
		boolean isTitleValid = true;
		boolean isDescriptionValid = true;
		boolean areSubtitlesValid = true;		
		boolean areSectionsValid = true;
		
		if(!validator.isTitleValid(title)) {
			session.setAttribute("wrongTitle", true);
			isTitleValid = false;
		}
		
		if(!validator.isDescriptionValid(description)) {
			session.setAttribute("wrongDescription", true);
			isDescriptionValid = false;
		}
			
		if(validator.hasEmptyValues(sectionsTitle, 1)) {
			session.setAttribute("wrongSectionTitle", true);
			areSubtitlesValid = false;
		}
		
		if(validator.hasEmptyValues(sectionsContent, 50)) {
			session.setAttribute("wrongSectionContent", true);
			areSectionsValid = false;
		}
		
		if(isTitleValid && isDescriptionValid && areSubtitlesValid && areSectionsValid) {
			return true;
		}
		return false;
	}
	
	private void saveArticleAfterEdit(Article selectedArticle, String title, String description, String sectionsTitle, String sectionsContent, HttpSession session) {
		selectedArticle.setTitle(title);
		selectedArticle.setDescription(description);
		selectedArticle.setSections(this.getArticleSectionsToAdd(sectionsTitle, sectionsContent));
		List<ArticleComment> comments = new ArrayList<ArticleComment>();
		selectedArticle.setComments(comments);
		
		if(selectedArticle.getComments().size() > 0) {
			comments = selectedArticle.getComments();
		}
		
		articleRepository.save(selectedArticle);
		session.setAttribute("isUserAllowedToEdit", null);
	}
	
	private void verifyArticleComponent(String component, HttpSession session, Model model) {
		if(session.getAttribute(component) != null) {
			model.addAttribute(component, true);
			session.setAttribute(component, null);
		}
	}
	
	private boolean isArticleAlreadyLikedByUser(Article article, UserEntity user) {
		for(Article articleObject : articleRepository.findAll()) {
			for(ArticleLike like : articleObject.getLikes()) {
				if(like.getArticleId() == article.getArticleId() && like.getUserId() == user.getId()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void removeLikeForArticle(UserEntity user, Article article) {
		List<ArticleLike> likes = article.getLikes();

		for(ArticleLike like : likes) {
			if(like.getUserId() == user.getId() && like.getArticleId() == article.getArticleId()) {
				likes.remove(like);
				break;
			}
		}
		
		article.setLikes(likes);
		articleRepository.save(article);
	}
	
	private void addLikeForArticle(UserEntity user, Article article) {
		List<ArticleLike> likes = article.getLikes();
		likes.add(new ArticleLike(user.getId(), article.getArticleId()));
		article.setLikes(likes);
		articleRepository.save(article);
	}
	
	private List<ArticleSection> getArticleSectionsToAdd(String subtitle, String sectionsContent){
		List<ArticleSection> articleSections = new ArrayList<>();
		
		List<String> subtitles = new ArrayList<>(Arrays.asList(subtitle.split(",")));
		List<String> sections = new ArrayList<>(Arrays.asList(sectionsContent.split(",")));
		
		for(int index = 0; index < subtitles.size(); index++) {
			ArticleSection articleSection = new ArticleSection(subtitles.get(index), sections.get(index));
			articleSections.add(articleSection);
		}
		return articleSections;
	}
	
	private Integer getLastArticleId() {
		List<Integer> articleIds = new ArrayList<>();
		for(Article article : articleRepository.findAll()) {
			articleIds.add(article.getArticleId());
		}
		
		if(articleIds.size() > 0) {
			return this.getMaxIdFromList(articleIds);
		}
		return 0;		
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
		List<ArticleLikeDTO> likesDTO = new ArrayList<>();
		
		for(ArticleComment comment : article.getComments()) {
			commentsDTO.add(this.convertFromArticleCommentToArticleCommentDTO(comment));
		}
		
		for(ArticleLike like : article.getLikes()) {
			likesDTO.add(this.convertFromArticleLikeToArticleLikeDTO(like));
		}
		
		Collections.reverse(commentsDTO);
		
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), this.getUserById(article.getUserId()), article.getDate(), article.getTitle(), likesDTO, 
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
	
	private ArticleLikeDTO convertFromArticleLikeToArticleLikeDTO(ArticleLike like) {
		ArticleLikeDTO articleLikeDTO = new ArticleLikeDTO(this.getUserById(like.getUserId()), this.getArticleById(like.getArticleId()));
		return articleLikeDTO;
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
