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

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public String getUserHome(Model model, Principal principal, HttpSession session) {
//		if (session.isNew())
//			throw new NewSessionException();		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = loginedUser.toString();
		model.addAttribute("user", userInfo);

		return "views/user/userMainPage";
	}
}
