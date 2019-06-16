package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommunityController {

	@GetMapping("/community")
	private String getComunityPage() {
		return "views/all/comunitateView";
	}
	
	@PostMapping("/community")
	private String comunityPageActions() {
		return "views/all/comunitateView";
	}
}
