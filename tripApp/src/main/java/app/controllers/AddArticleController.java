package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
import app.documents.ArticleComment;
import app.documents.ArticleLike;
import app.documents.ArticleSection;
import app.entities.UserEntity;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;

@Controller
public class AddArticleController {
	
	@Autowired 
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/add-article")
	public String getAddArticlePage() {
		return "views/staff/addArticleView";
	}
	
	@PostMapping("/add-article")
	public String addArticleActions(Model model, Principal principal, 
									@RequestParam(name = "title", required = false) String title,
									@RequestParam(name = "subtitle", required = false) String subtitles,
									@RequestParam(name = "sectionContent", required = false) String sectionsContent,
									@RequestParam(name = "description", required = false) String description) {
		
		UserEntity user = this.getUserByEmail(principal.getName());
		
		Article article = new Article();
		article.setArticleId(this.getLastArticleId() + 1);
		article.setUserId(user.getId());
		article.setTitle(title);
		article.setDate(LocalDate.now().toString());
		article.setDescription(description);
		article.setSections(this.getArticleSectionsToAdd(subtitles, sectionsContent));
		List<ArticleLike> likes = new ArrayList<ArticleLike>();
		article.setLikes(likes);
		List<ArticleComment> comments = new ArrayList<ArticleComment>();
		article.setComments(comments);
		
		if(!this.hasUserAlreadyPostedForCurrentDate(LocalDate.now().toString(), user)) {
			articleRepository.save(article);
			model.addAttribute("articleSuccessfullyAdded", true);		
		}
		else {
			model.addAttribute("aleadyPostedForToday", true);
		}
					
		return "views/staff/addArticleView";
	}
	
	private boolean hasUserAlreadyPostedForCurrentDate(String date, UserEntity user) {
		for(Article article : articleRepository.findAll()) {
			if(article.getUserId() == user.getId() && article.getDate().equals(date)) {
				return true;
			}
		}
		return false;
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
	
	private Integer getMaxIdFromList(List<Integer> articleIds) {
		Integer max = articleIds.get(0);
		for(int index = 1; index < articleIds.size(); index++) {
			if(articleIds.get(index) > max) {
				max = articleIds.get(index);
			}
		}
		return max;	
	}
	
	private UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();
		
		for(UserEntity user : users) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
