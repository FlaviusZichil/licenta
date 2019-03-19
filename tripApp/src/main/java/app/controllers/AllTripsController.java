package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

import app.dto.PeakDTO;
import app.dto.TripDTO;
import app.entities.Trip;
import app.models.TripViewModel;
import app.repositories.TripRepository;

@Controller
public class AllTripsController {

	@Autowired
	private TripRepository tripRepository;
	
	@SessionScope
	@GetMapping("/all-trips")
	public String getAllTripsPage(Model model, TripViewModel tripViewModel, HttpSession session, Principal principal) {
		
		tripViewModel.setTripsDTO(this.getAllTripsDTO());
		model.addAttribute("tripViewModel", tripViewModel);	
		
//		User loginedUser = (User) ((Authentication) principal).getPrincipal();
//		this.verifyUserRole(loginedUser, model);
		
		return "views/all/allTrips";
	}
	
//	private void verifyUserRole(User user, Model model) {
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//			model.addAttribute("isAdmin", true);
//		}		
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUIDE"))) {
//			model.addAttribute("isGuide", true);
//		}		
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
//			model.addAttribute("isStaff", true);
//		}		
//		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
//			model.addAttribute("isUser", true);
//		}
//	}
	
	private List<TripDTO> getAllTripsDTO(){
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<TripDTO> allTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : allTrips) {
			if(trip.getStatus().equals("Active")) {
				PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(), trip.getPeak().getAltitude(), trip.getPeak().getCity(), 
						  trip.getPeak().getTrips(), trip.getPeak().getMountain());
				allTripsDTO.add(new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
						trip.getDifficulty(), trip.getUsers(), trip.getRoute(), peakDTO));
			}			
		}
		return allTripsDTO;
	}
}
