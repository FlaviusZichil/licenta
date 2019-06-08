package app.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.entities.UserEntity;
import app.utils.UserUtils;

@Controller
public class LoginController {
	@Autowired
	private UserUtils userUtils;
	
	@GetMapping("/login")
	public String login() {
		return "views/all/login";
	}
	
	@PostMapping("/login")
	public String loginActions(Principal principal, Model model) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		return "views/all/login";
	}
}
