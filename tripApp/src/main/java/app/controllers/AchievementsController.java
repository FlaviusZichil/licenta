package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AchievementsController {
	@GetMapping("/achievements")
	public String getAchievements() {
		return "views/all/achievementsView";
	}
	
	@PostMapping("/achievements")
	public String achievementsActions() {
		return "views/all/achievementsView";
	}
}
