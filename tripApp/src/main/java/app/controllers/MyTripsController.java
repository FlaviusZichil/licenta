package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
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
import app.entities.Guide;
import app.entities.Trip;
import app.entities.UserEntity;
import app.entities.UserTrip;
import app.models.TripViewModel;
import app.repositories.GuideRepository;
import app.repositories.TripRepository;
import app.repositories.UserTripRepository;
import app.utils.Conversion;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class MyTripsController {	
	@Autowired
	private UserTripRepository userTripRepository;	
	@Autowired
	private UserUtils userUtils;	
	@Autowired
	private TripRepository tripRepository;	
	@Autowired
	private Conversion conversion;
	
	@GetMapping("/my-trips")
	public String getUserTrips(Model model, TripViewModel tripViewModel, Principal principal) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
		if(user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_STAFF")) {
			tripViewModel.setTripsDTO(userUtils.getAllTripsDTOForUser(user));			
			if(userUtils.getAllTripsDTOForUser(userUtils.getUserByEmail(principal.getName())).isEmpty()) {
				model.addAttribute("hasUserTrips", false);
			}
			model.addAttribute("tripViewModel", tripViewModel);
		}
		
		if(user.getRole().getName().equals("ROLE_GUIDE")) {
			tripViewModel.setTripsDTO(getAllTripsDTOForGuide(user.getGuide()));			
			if(getAllTripsDTOForGuide(user.getGuide()).isEmpty()) {
				model.addAttribute("hasUserTrips", false);
			}		
			if(user.getRole().getName().equals("ROLE_GUIDE") && TripUtils.getNumberOfFinishedTripsWithActiveStatusForGuide(user.getGuide()) > 0) {
				model.addAttribute("guideHasUnfinishedTrips", true);
			}
			
			model.addAttribute("tripViewModel", tripViewModel);
		}	
		return "views/all/mytrips";
	}

	@PostMapping("/my-trips")
	public String userTripsActions(Model model, TripViewModel tripViewModel, Principal principal,
								 @RequestParam MultiValueMap<String, String> tripToRemove) {
		
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
		for (Entry<String, List<String>> entry : tripToRemove.entrySet()) {
			String tripId = entry.getKey();
			Trip trip = getTripById(Integer.parseInt(tripId));
			if(!isUserRegisteredForTrip(user, Integer.parseInt(tripId))) {
				return "redirect:/my-trips"; 
			}	
			if(LocalDate.now().isAfter(LocalDate.parse(trip.getStartDate()))) {
				return "redirect:/my-trips";
			}
			if(user.getRole().getName().equals("ROLE_USER")) {
				removeTripForUser(user, Integer.parseInt(tripId));
			    increaseTripCapacity(Integer.parseInt(tripId));
			}
		}
		
		tripViewModel.setTripsDTO(userUtils.getAllTripsDTOForUser(userUtils.getUserByEmail(principal.getName())));
		model.addAttribute("tripViewModel", tripViewModel);		
		
		return "redirect:/my-trips";
	}

	private void addTripStartedVerifyOnModel(Model model, String tripId) {
		model.addAttribute("tripStarted", false);
		LocalDate startDate = LocalDate.parse(getTripById(Integer.parseInt(tripId)).getStartDate());
		if(startDate.isEqual(LocalDate.now()) || LocalDate.now().isAfter(startDate)) {
			model.addAttribute("tripStarted", true);
		}
	}
	
	private List<TripDTO> getAllTripsDTOForGuide(Guide guide) {
		List<TripDTO> tripsDTOForGuide = new ArrayList<>();
		for(Trip trip : guide.getTrips()) {
			tripsDTOForGuide.add(conversion.convertFromTripToTripDTO(trip));
		}
		return userUtils.sortTripsByStatus(tripsDTOForGuide);
	}
	
	private void increaseTripCapacity(Integer tripId) {
		Trip trip = tripRepository.findById(tripId).get();
		trip.setCapacity(trip.getCapacity() + 1);
		tripRepository.save(trip);
	}
	
	private void removeTripForUser(UserEntity user, Integer tripId) {
		Trip trip = getTripById(tripId);
		userTripRepository.delete(getUserTrip(user, trip));
	}
	
	private Trip getTripById(Integer tripId) {
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getId() == tripId) {
				return trip;
			}
		}
		return null;
	}
	
	private boolean isUserRegisteredForTrip(UserEntity user, Integer tripId) {
		Trip trip = getTripById(tripId);
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getUser().equals(user) && userTrip.getTrip().equals(trip)) {
				return true;
			}
		}
		return false;
	}
	
	private UserTrip getUserTrip(UserEntity user, Trip trip) {
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getTrip().equals(trip) && userTrip.getUser().equals(user)) {
				return userTrip;
			}
		}
		return null;
	}
}
