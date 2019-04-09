package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import app.entities.Tombola;
import app.entities.Trip;
import app.entities.UserEntity;
import app.repositories.TombolaRepository;
import app.repositories.UserRepository;
import app.utils.TripUtils;

@Controller
public class TombolaController {
	
	@Autowired
	private TombolaRepository tombolaRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/tombola")
	public String getTombola(Model model, Principal principal) {
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		LocalDate date = LocalDate.now();
		
		// date.getMonth() == APRIL
		if(!isUserAlreadyRegisteredForThisMonth(currentUser, date.getMonth().toString(), date.getYear())) {
			this.registerUserToTombola(currentUser, date);
		}
		else {
			System.out.println("Already registered for tshi month");
		}
		
		return "views/all/tombola";
	}
	
	private boolean isUserAlreadyRegisteredForThisMonth(UserEntity user, String month, Integer year) {
		Iterable<Tombola> registrations = tombolaRepository.findAll();
		List<Tombola> registrationsToTombola = TripUtils.convertFromIterableToList(registrations);
		
		if(registrationsToTombola.size() != 0) {
			for(Tombola registration : registrations) {
				LocalDate date = LocalDate.parse(registration.getDate());
				if(registration.getUser().equals(user) && date.getMonth().toString().equals(month) && date.getYear() == year) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void registerUserToTombola(UserEntity user, LocalDate date) {
		if(Integer.parseInt(user.getPoints()) >= 25) {
			tombolaRepository.save(new Tombola(date.toString(), "not winner", user));
			Integer userPoints = Integer.parseInt(user.getPoints()) - 25;
			user.setPoints(userPoints.toString());
			userRepository.save(user);
		}	
		else {
			System.out.println("not enough points");
		}
	}
	
	public UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();

		for (UserEntity user : users) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
