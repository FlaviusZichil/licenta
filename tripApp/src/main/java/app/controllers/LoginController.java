package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(Model model,
						@RequestParam(name = "success", required = false) String status) {
		if(status != null) {
			model.addAttribute("successfullLogin", false);
		}
		return "views/all/login";
	}
	
	@PostMapping("/login")
	public String loginActions(Model model) {
		return "views/all/login";
	}
}
