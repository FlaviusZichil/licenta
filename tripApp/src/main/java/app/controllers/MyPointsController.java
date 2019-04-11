package app.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import app.entities.UserEntity;
import app.repositories.UserRepository;

@Controller
public class MyPointsController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/my-points")
	public String getMyPoints(Model model, Principal principal) {
		
		UserEntity user = this.getUserByEmail(principal.getName());
		model.addAttribute("userPoints", user.getPoints());
		return "views/all/myPoints";
	}
	
	private UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();
		
		for(UserEntity user : users) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
