package app.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	@RequestMapping(value = { "/userHome" }, method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal, HttpSession session) {
//		if (session.isNew())
//			throw new NewSessionException();
		
		// returns user email
		String userName = principal.getName();

		System.out.println("User Name (Principal): " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		System.out.println(loginedUser.getAuthorities().toString());

		String userInfo = loginedUser.toString();
		model.addAttribute("user", userInfo);

		return "views/userHome";
	}
}
