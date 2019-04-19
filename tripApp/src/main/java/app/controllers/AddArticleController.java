package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.documents.Article;
import app.documents.ArticleSection;
import app.repositories.ArticleRepository;

@Controller
public class AddArticleController {
	
	@Autowired 
	private ArticleRepository articleRepository;

	@GetMapping("/add-article")
	public String getAddArticlePage() {
		
//		ArticleSection section1 = new ArticleSection(1, "subsection1", "sebsection content1");
//		ArticleSection section2 = new ArticleSection(2, "subsection2", "sebsection content2");
//		
//		List<ArticleSection> sections = new ArrayList<>();
//		sections.add(section1);
//		sections.add(section2);
//		
//		articleRepository.save(new Article(10, 1, "2020-01-05", "Titlu", sections));
		
//		for(Article article : articleRepository.findAll()) {
//			for(ArticleSection section : article.getSections()) {
//				System.out.println(section.toString());
//			}
//		}
		
		return "views/staff/addArticleView";
	}
	
	@PostMapping("/add-article")
	public String addArticleActions() {
		return "views/staff/addArticleView";
	}
}
