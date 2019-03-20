package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

import app.dto.PeakDTO;
import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.repositories.UserRepository;

@Controller
public class AllTripsController {

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@SessionScope
	@GetMapping("/all-trips")
	public String getAllTripsPage(Model model, TripViewModel tripViewModel, HttpSession session, Principal principal) {
		if(this.getAllTripsDTOAvailableForUser(principal).isEmpty()) {
			model.addAttribute("areTripsAvailable", false);
		}else {
			tripViewModel.setTripsDTO(this.getAllTripsDTOAvailableForUser(principal));
			model.addAttribute("tripViewModel", tripViewModel);	
		}		
		return "views/all/allTrips";
	}
	
	private List<TripDTO> getAllTripsDTOAvailableForUser(Principal principal){
		Iterable<Trip> allTrips = tripRepository.findAll();
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : allTrips) {
			if(trip.getStatus().equals("Active") && !currentUserTrips.contains(trip)) {
				PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(), trip.getPeak().getAltitude(), trip.getPeak().getCity(), 
						trip.getPeak().getTrips(), trip.getPeak().getMountain());
				currentUserTripsDTO.add(new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
						trip.getDifficulty(), trip.getUsers(), trip.getRoute(), peakDTO));
			}			
		}
		return currentUserTripsDTO;
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
