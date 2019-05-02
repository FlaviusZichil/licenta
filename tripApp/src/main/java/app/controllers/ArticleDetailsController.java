package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
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
	public String getArticle(Model model, ArticleDetailsViewModel articleDetailsViewModel,
						    @RequestParam(value = "a", required = false) String articleId) {

		Article article = this.getArticleById(Integer.parseInt(articleId));
		ArticleDTO articleDTO = this.convertFromArticleToArticleDTO(article);
		articleDetailsViewModel.setArticleDTO(articleDTO);
		
		System.out.println(articleDTO.getTitle());
		System.out.println(articleDTO.getDescription());		
		System.out.println(articleDTO.getDate());	
		System.out.println(articleDTO.getLikes());
		
		
		model.addAttribute("articleDetailsViewModel", articleDetailsViewModel);
		
		return "views/all/articleDetailsView";
	}
	
	@PostMapping("/article")
	public String articleActions() {
		return "views/all/articleDetailsView";
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
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), this.getUserById(article.getUserId()), article.getDate(), article.getTitle(), article.getLikes(), article.getDescription(), article.getSections());
		return articleDTO;
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
