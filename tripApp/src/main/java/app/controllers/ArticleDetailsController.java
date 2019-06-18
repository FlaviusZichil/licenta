package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import app.dto.ArticleDTO;
import app.entities.UserEntity;
import app.models.ArticleDetailsViewModel;
import app.repositories.ArticleRepository;
import app.utils.ArticleUtils;
import app.utils.Conversion;
import app.utils.UserUtils;
import app.validators.ArticleValidator;

@Controller
public class ArticleDetailsController {	
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private ArticleUtils articleUtils;
	@Autowired
	private Conversion conversion;

	@GetMapping("/article")
	public String getArticle(Model model, ArticleDetailsViewModel articleDetailsViewModel, HttpSession session, Principal principal,
						    @RequestParam(value = "a", required = false) String articleId) {
		
		if(articleId == null) {
			return "redirect:/all-articles";
		}
		
		Article article = articleUtils.getArticleById(Integer.parseInt(articleId));
		ArticleDTO articleDTO = conversion.convertFromArticleToArticleDTO(article);
		articleDetailsViewModel.setArticleDTO(articleDTO);		
		model.addAttribute("articleDetailsViewModel", articleDetailsViewModel);
		
		UserEntity user = userUtils.getUserByEmail(principal.getName());
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
		
		Article selectedArticle = articleUtils.getArticleById(Integer.parseInt((String) session.getAttribute("articleId")));
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
		if(submitInputs != null) {
			for(Map.Entry<String, List<String>> input : submitInputs.entrySet()){
					if(input.getValue().contains("Sterge")){
						if(getArticleThatContainsComment(Integer.parseInt(input.getKey())) != null) {
							removeComment(Integer.parseInt(input.getKey()));
						}
						break;
					}
					if(input.getValue().contains("Elimina sectiunea")) {
						// key is like: sectionTitle, sectionContent
						List<String> sectionComponents = new ArrayList<>(Arrays.asList(input.getKey().split(",")));
						String sectionTitle = sectionComponents.get(0); 
						String sectionContent = sectionComponents.get(1);
						removeSection(sectionTitle, sectionContent);
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
					if(ArticleValidator.isArticleValid(title, description, sectionsTitle, sectionsContent, session)) {
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
	
	private void removeSection(String sectionTitle, String sectionContent) {
		for(Article article : articleRepository.findAll()) {
			List<ArticleSection> sections = article.getSections();
			
			for(ArticleSection section : sections) {
				if(section.getSectionTitle().trim().equals(sectionTitle.trim()) && section.getSectionContent().trim().equals(sectionContent.trim())) {
					sections.remove(section);
					break;
				}
			}
			article.setSections(sections);
			articleRepository.save(article);
		}
	}
	
	private void saveArticleAfterEdit(Article selectedArticle, String title, String description, String sectionsTitle, String sectionsContent, HttpSession session) {
		selectedArticle.setTitle(title);
		selectedArticle.setDescription(description);
		selectedArticle.setSections(articleUtils.getArticleSectionsToAdd(sectionsTitle, sectionsContent));
		
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
		if(!isArticleAlreadyLikedByUser(article, user)) {
			List<ArticleLike> likes = article.getLikes();
			likes.add(new ArticleLike(user.getId(), article.getArticleId()));
			article.setLikes(likes);
			articleRepository.save(article);
		}		
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
		Article article = getArticleThatContainsComment(commentId);
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
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		Article article = articleUtils.getArticleById(articleId);
		ArticleComment comment = new ArticleComment(this.getLastCommentId() + 1, user.getId(), LocalDate.now().toString(), commentContent);

		List<ArticleComment> comments = article.getComments();		
		comments.add(comment);	
		articleRepository.save(article);	
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
}
