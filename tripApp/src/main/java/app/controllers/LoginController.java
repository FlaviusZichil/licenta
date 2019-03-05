package app.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	
	@RequestMapping(value = { "/loginDispacher" }, method = RequestMethod.GET)
	public String loginDIspacher(Model model, Principal principal, HttpSession session) {
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		
		if(loginedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "redirect:/adminHome";
		}
		
		if(loginedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUIDE"))) {
			return "redirect:/guideHome";
		}
		
		if(loginedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
			return "redirect:/staffHome";
		}
		
		if(loginedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			return "redirect:/userHome";
		}
		return "";
	}
}
