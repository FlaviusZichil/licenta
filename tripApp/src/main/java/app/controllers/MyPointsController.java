package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import app.entities.Tombola;
import app.entities.UserEntity;
import app.repositories.TombolaRepository;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class MyPointsController {
	
	@Autowired
	private TombolaRepository tombolaRepository;
	
	@Autowired
	private UserUtils userUtils;

	@GetMapping("/my-points")
	public String getMyPoints(Model model, Principal principal) {
		
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		model.addAttribute("userPoints", user.getPoints());
		model.addAttribute("usedPoints", this.getAllTombolaRegistrationForUser(user));
		return "views/all/myPoints";
	}
	
	private List<Tombola> getAllTombolaRegistrationForUser(UserEntity user) {
		List<Tombola> userRegistrations = new ArrayList<>();
		
		for(Tombola registration : tombolaRepository.findAll()) {
			if(registration.getUser().equals(user)) {
				userRegistrations.add(registration);
			}
		}
		return userRegistrations;
	}
}
