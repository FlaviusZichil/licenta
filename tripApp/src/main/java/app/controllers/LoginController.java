package app.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login() {
		return "views/login";
	}
	
//	@GetMapping("/userHome")
//	public String getUserHome() {
//		return "views/userHome";
//	}
	
	@GetMapping("/adminHome")
	public String getAdminHome() {
		return "views/adminHome";
	}
	
	@RequestMapping(value = { "/userHome" }, method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal, HttpSession session) {
//		if (session.isNew())
//			throw new NewSessionException();
		
		// return user email
		String userName = principal.getName();

		System.out.println("User Name (Principal): " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = loginedUser.toString();
		model.addAttribute("user", userInfo);

		return "views/userHome";
	}
	
	@GetMapping("/all")
	public String all() {
		return "views/all";
	}
}
