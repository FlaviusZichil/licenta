package app.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
import app.dto.ArticleDTO;
import app.models.AllArticlesViewModel;
import app.repositories.ArticleRepository;
import app.utils.Conversion;

@Controller
public class AllArticlesController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private Conversion conversion;

	@GetMapping("/all-articles")
	public String getAllArticles(Model model) {
		AllArticlesViewModel allArticlesViewModel = new AllArticlesViewModel();
		allArticlesViewModel.setArticlesDTO(this.getAllArticlesDTO());
		model.addAttribute("allArticlesViewModel", allArticlesViewModel);
		return "views/all/allArticles";
	}
	
	@PostMapping("/all-articles")
	public String allArticlesActions(Model model, AllArticlesViewModel allArticlesViewModel,
									 @RequestParam(name = "submit", required = false) String actionType,
									 @RequestParam(name = "articleFilter", required = false) String articleFilter) {
		switch(actionType) {
			case "Citeste mai mult":{
				return "redirect:/article";
			}
			case "Aplica filtrul": {
				if(articleFilter != null) {
					switch(articleFilter) {
						case "Cele mai recente":{
							allArticlesViewModel.setArticlesDTO(this.sortArticlesByDate("ascending"));
							break;
						}
						case "Cele mai populare":{
							allArticlesViewModel.setArticlesDTO(this.sortArticlesByLikes());
							break;
						}
						case "Cele mai vechi":{
							allArticlesViewModel.setArticlesDTO(this.sortArticlesByDate("descending"));
							break;
						}
					}
					model.addAttribute("allArticlesViewModel", allArticlesViewModel);
				}
				else {
					return "redirect:/all-articles";
				}
				break;
			}
		}
		return "views/all/allArticles";
	}
	
	private List<ArticleDTO> sortArticlesByDate(String way){
		List<ArticleDTO> articlesDTO = this.getAllArticlesDTO();
		Collections.sort(articlesDTO, new Comparator<ArticleDTO>(){
			   @Override
			   public int compare(ArticleDTO firstArticleDTO, ArticleDTO secondArticleDTO) {			   
				   LocalDate firstArticleDTODate = LocalDate.parse(firstArticleDTO.getDate());
				   LocalDate secondArticleDTODate = LocalDate.parse(secondArticleDTO.getDate());
				   
				   if(firstArticleDTODate.isBefore(secondArticleDTODate)) {
					   if(way.equals("ascending")) {
						   return 1;
					   }
					   if(way.equals("descending")) {
						   return -1;
					   }				   
				   }				   
				   if(firstArticleDTODate.isAfter(secondArticleDTODate)) {
					   if(way.equals("ascending")) {
						   return -1;
					   }
					   if(way.equals("descending")) {
						   return 1;
					   }
				   }
				   return 0;
			     }
			 });
		return articlesDTO;
	}
	
	private List<ArticleDTO> sortArticlesByLikes(){
		List<ArticleDTO> articlesDTO = this.getAllArticlesDTO();
		Collections.sort(articlesDTO, new Comparator<ArticleDTO>(){
			   @Override
			   public int compare(ArticleDTO firstArticleDTO, ArticleDTO secondArticleDTO) {			   				   
				   if(firstArticleDTO.getLikesDTO().size() > secondArticleDTO.getLikesDTO().size()) {
					   return -1;			   
				   }				   
				   if(firstArticleDTO.getLikesDTO().size() < secondArticleDTO.getLikesDTO().size()) {
					   return 1;
				   }
				   return 0;
			     }
			 });
		return articlesDTO;
	}
	
	private List<ArticleDTO> getAllArticlesDTO(){
		List<ArticleDTO> articlesDTO = new ArrayList<>();
		for(Article article : articleRepository.findAll()) {
			articlesDTO.add(conversion.convertFromArticleToArticleDTO(article));
		}
		return articlesDTO;
	}
}
