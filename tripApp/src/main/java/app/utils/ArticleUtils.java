package app.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.documents.Article;
import app.documents.ArticleSection;
import app.repositories.ArticleRepository;

@Component
public class ArticleUtils {

	@Autowired
	private ArticleRepository articleRepository;
	
	public List<ArticleSection> getArticleSectionsToAdd(String subtitle, String sectionsContent){
		List<ArticleSection> articleSections = new ArrayList<>();
		
		List<String> subtitles = new ArrayList<>(Arrays.asList(subtitle.split(",")));
		List<String> sections = new ArrayList<>(Arrays.asList(sectionsContent.split(",")));
		
		for(int index = 0; index < subtitles.size(); index++) {
			ArticleSection articleSection = new ArticleSection(subtitles.get(index), sections.get(index));
			articleSections.add(articleSection);
		}
		return articleSections;
	}
	
	public Article getArticleById(Integer articleId) {
		for(Article article : articleRepository.findAll()) {
			if(article.getArticleId() == articleId) {
				return article;
			}
		}
		return null;
	}
}
