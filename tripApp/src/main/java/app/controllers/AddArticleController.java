package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddArticleController {

	@GetMapping("/add-article")
	public String getAddArticlePage() {
		return "";
	}
	
	@PostMapping("/add-article")
	public String addArticleActions() {
		return "";
	}
}
