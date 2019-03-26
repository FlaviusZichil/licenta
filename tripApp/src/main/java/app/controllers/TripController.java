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

import app.dto.PeakDTO;
import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.repositories.UserRepository;

@Controller
public class TripController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@GetMapping("/my-trips")
	public String getUserTrips(Model model, TripViewModel tripViewModel, Principal principal) {
		tripViewModel.setTripsDTO(this.getAllTripsDTOForUser(principal));
		
		if(this.getAllTripsDTOForUser(principal).isEmpty()) {
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
		
		tripViewModel.setTripsDTO(this.getAllTripsDTOForUser(principal));
		model.addAttribute("tripViewModel", tripViewModel);		
		
		return "redirect:/my-trips";
	}
	
	private void increaseTripCapacity(Integer tripId) {
		Trip trip = tripRepository.findById(tripId).get();
			trip.setCapacity(trip.getCapacity() + 1);
			tripRepository.save(trip);
	}
	
	private List<TripDTO> getAllTripsDTOForUser(Principal principal){
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : currentUserTrips) {
			PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(), trip.getPeak().getAltitude(), trip.getPeak().getCity().getName(), 
						      trip.getPeak().getTrips(), trip.getPeak().getMountain());
			currentUserTripsDTO.add(new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
						        trip.getDifficulty(), trip.getUsers(), trip.getRoute(), peakDTO));
						
		}
		return currentUserTripsDTO;
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
