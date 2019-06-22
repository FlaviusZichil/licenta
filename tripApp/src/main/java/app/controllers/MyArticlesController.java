package app.controllers;

import java.security.Principal;
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
import app.dto.ArticleDTO;
import app.entities.UserEntity;
import app.models.AllArticlesViewModel;
import app.repositories.ArticleRepository;
import app.utils.Conversion;
import app.utils.UserUtils;

@Controller
public class MyArticlesController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private Conversion conversion;

	@GetMapping("/my-articles")
	public String getMyArticles(Model model, Principal principal) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
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
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
		if(articleToRemove != null) {
			for(Map.Entry<String, List<String>> articleId : articleToRemove.entrySet()){
				if(articleId.getValue().contains("Sterge articolul")){
					removeArticleForUser(Integer.parseInt(articleId.getKey()), user);
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
			}
		}
	}
	
	private List<ArticleDTO> getArticlesDTOForUser(UserEntity user){
		List<ArticleDTO> articlesDTOForUser = new ArrayList<>();
		
		for(Article article : articleRepository.findAll()) {
			if(article.getUserId() == user.getId()) {
				articlesDTOForUser.add(conversion.convertFromArticleToArticleDTO(article));
			}
		}
		return articlesDTOForUser;
	}
}
