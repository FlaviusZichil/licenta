package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripDetailsViewModel;
import app.repositories.TripRepository;
import app.repositories.UserRepository;

@Controller
public class TripDetailsController {

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@SessionScope
	@GetMapping("/trip-details")
	public String getTripDetails(Model model, Principal principal, TripDetailsViewModel tripDetailsViewModel, HttpSession session, 
								@RequestParam(value = "trip", required = false) String tripId) {
		
		tripDetailsViewModel.setTripDTO(this.getTripDTOById(Integer.parseInt(tripId)));
		model.addAttribute("tripDetailsViewModel", tripDetailsViewModel);
		
		session.setAttribute("tripId", tripId);
	
		return "views/all/tripDetails";
	}
	
	@PostMapping("/trip-details")
	public String postTripDetails(HttpSession session, Principal principal) {
		Integer tripId = Integer.parseInt((String) session.getAttribute("tripId"));
		this.addTripForUser(principal, tripId);
		return "redirect:/user";
	}
	
	private TripDTO getTripDTOById(Integer tripId) {
		TripDTO tripDTO = null;
		
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<Trip> trips = this.convertFromIterableToList(allTrips);
		
		for(Trip trip : trips) {
			if(trip.getId() == tripId) {
				tripDTO = new TripDTO(trip);
			}
		}
		return tripDTO;
	}
	
	private List<Trip> convertFromIterableToList(Iterable<Trip> allTrips){
		List<Trip> trips = new ArrayList<Trip>();
		
		for(Trip trip : allTrips) {
			trips.add(trip);
		}
		return trips;
	}
	
	private void addTripForUser(Principal principal, Integer tripId) {
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		UserEntity user = this.getUserByEmail(loginedUser.getUsername());
		
		List<Trip> trips = new ArrayList<Trip>();
		trips.add(this.getTripById(tripId));		
		user.setTrips(trips);
		
		userRepository.save(user);	
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
	
	private Trip getTripById(Integer tripId) {
		Iterable<Trip> trips = tripRepository.findAll();
		
		for(Trip trip : trips) {
			if(trip.getId() == tripId) {
				return trip;
			}
		}
		return null;
	}
}
