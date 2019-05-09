package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
import app.documents.ArticleComment;
import app.documents.ArticleLike;
import app.entities.UserEntity;
import app.repositories.ArticleRepository;
import app.utils.ArticleUtils;
import app.utils.Conversion;
import app.utils.UserUtils;
import app.validators.ArticleValidator;

@Controller
public class AddArticleController {
	
	@Autowired 
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ArticleUtils articleUtils;
	
	@Autowired
	private Conversion conversion;

	@GetMapping("/add-article")
	public String getAddArticlePage(Model model, HttpSession session) {
		
		if(session.getAttribute("articleToAdd") != null) {
			Article article = (Article) session.getAttribute("articleToAdd");
			model.addAttribute("isArticleValid", false);
			model.addAttribute("articleToAddDTO", conversion.convertFromArticleToArticleDTO(article));
			session.setAttribute("articleToAdd", null);
		}
		
		this.verifyArticleComponent("wrongTitle", session, model);
		this.verifyArticleComponent("wrongDescription", session, model);
		this.verifyArticleComponent("wrongSectionTitle", session, model);
		this.verifyArticleComponent("wrongSectionContent", session, model);

		return "views/staff/addArticleView";
	}
	
	@PostMapping("/add-article")
	public String addArticleActions(Model model, Principal principal, HttpSession session,
									@RequestParam(name = "title", required = false) String title,
									@RequestParam(name = "subtitle", required = false) String subtitles,
									@RequestParam(name = "sectionContent", required = false) String sectionsContent,
									@RequestParam(name = "description", required = false) String description) {
		
		UserEntity user = userUtils.getUserByEmail(principal.getName());	
		Article article = this.createArticleForUser(user, title, description, subtitles, sectionsContent);
		
		if(!this.hasUserAlreadyPostedForCurrentDate(LocalDate.now().toString(), user) && ArticleValidator.isArticleValid(title, description, subtitles, sectionsContent, session)) {
			articleRepository.save(article);
			model.addAttribute("articleSuccessfullyAdded", true);
			session.setAttribute("articleToAdd", null);
		}
		else {
			model.addAttribute("aleadyPostedForToday", true);
			session.setAttribute("articleToAdd", article);
			session.setAttribute("userHasAlreadyPosted", true);
		}
		
		return "redirect:/add-article";
	}
	
	private void verifyArticleComponent(String component, HttpSession session, Model model) {
		if(session.getAttribute(component) != null) {
			model.addAttribute(component, true);
			session.setAttribute(component, null);
		}
	}
	
	private Article createArticleForUser(UserEntity user, String title, String description, String subtitles, String sectionsContent) {
		Article article = new Article();
		article.setArticleId(this.getLastArticleId() + 1);
		article.setUserId(user.getId());
		article.setTitle(title);
		article.setDate(LocalDate.now().toString());
		article.setDescription(description);
		article.setSections(articleUtils.getArticleSectionsToAdd(subtitles, sectionsContent));
		List<ArticleLike> likes = new ArrayList<ArticleLike>();
		article.setLikes(likes);
		List<ArticleComment> comments = new ArrayList<ArticleComment>();
		article.setComments(comments);
		
		return article;
	}
	
	private boolean hasUserAlreadyPostedForCurrentDate(String date, UserEntity user) {
		for(Article article : articleRepository.findAll()) {
			if(article.getUserId() == user.getId() && article.getDate().equals(date)) {
				return true;
			}
		}
		return false;
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
}
