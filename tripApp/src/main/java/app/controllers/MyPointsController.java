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
import app.repositories.UserRepository;
import app.utils.TripUtils;

@Controller
public class MyPointsController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TombolaRepository tombolaRepository;

	@GetMapping("/my-points")
	public String getMyPoints(Model model, Principal principal) {
		
		UserEntity user = this.getUserByEmail(principal.getName());
		model.addAttribute("userPoints", user.getPoints());
		model.addAttribute("usedPoints", this.getAllTombolaRegistrationForUser(user));
		return "views/all/myPoints";
	}
	
	private List<Tombola> getAllTombolaRegistrationForUser(UserEntity user) {
		Iterable<Tombola> registrationsToTombola = tombolaRepository.findAll();
		List<Tombola> registrations = TripUtils.convertFromIterableToList(registrationsToTombola);
		List<Tombola> userRegistrations = new ArrayList<>();
		
		for(Tombola registration : registrations) {
			if(registration.getUser().equals(user)) {
				userRegistrations.add(registration);
			}
		}
		return userRegistrations;
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
