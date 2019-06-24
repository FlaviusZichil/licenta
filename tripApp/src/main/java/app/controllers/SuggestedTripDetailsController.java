package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripDetailsViewModel;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.utils.Conversion;
import app.utils.UserUtils;

@Controller
public class SuggestedTripDetailsController {
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private Conversion conversion;
	@Autowired
	private UserUtils userUtils;

	@GetMapping("/suggested-trip-details")
	public String getSuggestedTripsPage(Model model, TripDetailsViewModel tripDetailsViewModel, HttpSession session,
										@RequestParam(name = "trip", required = false) String tripId) {
		if(tripId == null) {
			return "redirect:/suggested-trips";
		}
		if(!getSuggestedTripsIds().contains(Integer.parseInt(tripId))) {
			return "redirect:/suggested-trips";
		}
		loadTrip(tripDetailsViewModel, model, Integer.parseInt(tripId));
		session.setAttribute("tripId", tripId);
		return "views/guide/suggestedTripDetailsView";
	}
	
	@PostMapping("/suggested-trip-details")
	public String suggestedTripsActions(Model model, TripDetailsViewModel tripDetailsViewModel, Principal principal, HttpSession session, 
										@RequestParam(name = "submit", required = false) String actionType,
										@RequestParam(name = "tripId", required = false) String tripId,
										@RequestParam(name = "tripCapacity", required = false) String tripCapacity,
										@RequestParam(name = "tripDifficulty", required = false) String tripDifficulty,
										@RequestParam(name = "tripPoints", required = false) String tripPoints) {
		
		if(!getSuggestedTripsIds().contains(Integer.parseInt(tripId))) {
			return "redirect:/suggested-trips";
		}
		
		UserEntity currentUser = userUtils.getUserByEmail(principal.getName());
		Trip tripToSave = getTripById(Integer.parseInt(tripId));

		if(actionType != null) {
			switch(actionType) {
				case "Organizeaza ascensiunea": {
					if(tripToSave.getStatus().equals("Suggested")) {
						organiseTrip(tripToSave, tripCapacity, tripDifficulty, tripPoints, currentUser);
					}
					return "redirect:/all-trips";
				}
				case "Salveaza modificarile si organizeaza ascensiunea": {				
					if(isCapacityValid(tripCapacity)) {
						tripToSave.setCapacity(Integer.parseInt(tripCapacity));
					}
					else {
						model.addAttribute("invalidCapacity", true);
					}
					
					if(isDifficultyValid(tripDifficulty)) {
						tripToSave.getRoute().setDifficulty(tripDifficulty);
					}
					else {
						model.addAttribute("invalidDifficulty", true);
					}
					
					if(arePointsValid(tripPoints)) {
						tripToSave.setPoints(Integer.parseInt(tripPoints));
					}
					else {
						model.addAttribute("invalidPoints", true);
					}
					
					if(isCapacityValid(tripCapacity) && isDifficultyValid(tripDifficulty) && arePointsValid(tripPoints) && tripToSave.getStatus().equals("Suggested")) {
						organiseTrip(tripToSave, tripCapacity, tripDifficulty, tripPoints, currentUser);
						return "redirect:/all-trips";
					}				
					break;
				}
				case "Renunta": {
					return "redirect:/suggested-trip-details?trip=" + tripId;
				}
			}
		}
		loadTrip(tripDetailsViewModel, model, Integer.parseInt((String) session.getAttribute("tripId")));
		return "redirect:/suggested-trips";
	}
	
	private List<Integer> getSuggestedTripsIds() {
		List<Integer> ids = new ArrayList<>();
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getStatus().equals("Suggested")) {
				ids.add(trip.getId());
			}
		}
		return ids;
	}

	private void loadTrip(TripDetailsViewModel tripDetailsViewModel, Model model, Integer tripId) {
		tripDetailsViewModel.setTripDTO(conversion.convertFromTripToTripDTO(getTripById(tripId)));
		model.addAttribute("tripDetailsViewModel", tripDetailsViewModel);
	}
	
	private void organiseTrip(Trip tripToSave, String tripCapacity, String tripDifficulty, String tripPoints, UserEntity currentUser) {
		tripToSave.setCapacity(Integer.parseInt(tripCapacity));
		tripToSave.getRoute().setDifficulty(tripDifficulty);
		tripToSave.setPoints(Integer.parseInt(tripPoints));
		tripToSave.setGuide(currentUser.getGuide());
		tripToSave.setStatus("Active");
		tripRepository.save(tripToSave);
	}
	
	private boolean arePointsValid(String tripPoints) {
		if(Integer.parseInt(tripPoints) < 50 || Integer.parseInt(tripPoints) > 75) {
			return false;
		}
		return true;
	}

	private boolean isDifficultyValid(String tripDifficulty) {
		if(!tripDifficulty.equals("Usor") && !tripDifficulty.equals("Mediu") && !tripDifficulty.equals("Dificil")) {
			return false;
		}
		return true;
	}

	private boolean isCapacityValid(String tripCapacity) {
		if(Integer.parseInt(tripCapacity) < 5 || Integer.parseInt(tripCapacity) > 50) {
			return false;
		}
		return true;
	}
	
	private Trip getTripById(Integer tripId) {
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getId() == tripId) {
				return trip;
			}
		}
		return null;
	}
}
