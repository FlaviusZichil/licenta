package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.documents.Article;
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
	public String allArticlesActions() {
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
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), this.getUserById(article.getUserId()), article.getDate(), article.getTitle(), article.getLikes(), article.getDescription(), article.getSections());
		return articleDTO;
	}
}
