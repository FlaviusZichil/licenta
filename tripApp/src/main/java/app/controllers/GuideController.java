package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuideController {

	@GetMapping("/guideHome")
	public String getGuideHome() {
		return "views/guideHome";
	}
}
