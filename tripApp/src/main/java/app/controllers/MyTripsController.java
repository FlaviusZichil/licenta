package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.repositories.UserRepository;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class MyTripsController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@GetMapping("/my-trips")
	public String getUserTrips(Model model, TripViewModel tripViewModel, Principal principal) {
		tripViewModel.setTripsDTO(UserUtils.getAllTripsDTOForUser(this.getUserByEmail(principal.getName())));
		
		if(UserUtils.getAllTripsDTOForUser(this.getUserByEmail(principal.getName())).isEmpty()) {
			model.addAttribute("hasUserTrips", false);
		}
		model.addAttribute("tripViewModel", tripViewModel);
		return "views/all/mytrips";
	}
	
	@PostMapping("/my-trips")
	public String userTripsActions(Model model, TripViewModel tripViewModel, Principal principal,
								 @RequestParam MultiValueMap<String, String> tripToRemove) {
		
		for (Entry<String, List<String>> entry : tripToRemove.entrySet()) {
		    String tripId = entry.getKey();
		    this.removeTripForUser(principal, Integer.parseInt(tripId));
		    this.increaseTripCapacity(Integer.parseInt(tripId));
		}
		
		tripViewModel.setTripsDTO(UserUtils.getAllTripsDTOForUser(this.getUserByEmail(principal.getName())));
		model.addAttribute("tripViewModel", tripViewModel);		
		
		return "redirect:/my-trips";
	}
	
	private void increaseTripCapacity(Integer tripId) {
		Trip trip = tripRepository.findById(tripId).get();
			trip.setCapacity(trip.getCapacity() + 1);
			tripRepository.save(trip);
	}
	
	private void removeTripForUser(Principal principal, Integer tripId) {
		UserEntity user = this.getUserByEmail(principal.getName());
		List<Trip> userTrips = user.getTrips();
		
		if(!userTrips.isEmpty()) {
			userTrips.remove(tripRepository.findById(tripId).get());
			user.setTrips(userTrips);
			userRepository.save(user);
		}
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
