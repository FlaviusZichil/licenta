package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.utils.Conversion;
import app.utils.UserUtils;

@Controller
public class SuggestedTripsController {
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private Conversion conversion;
	@Autowired
	private UserUtils userUtils;

	@GetMapping("/suggested-trips")
	public String getSuggestedTripsPage(Model model, TripViewModel tripViewModel) {
		loadAllTrips(tripViewModel, model);
		return "views/guide/suggestedTripsView";
	}
	
	@PostMapping("/suggested-trips")
	public String suggestedTripsActions(Model model, TripViewModel tripViewModel, Principal principal, 
										@RequestParam(name = "submit", required = false) String actionType,
										@RequestParam(name = "tripId", required = false) String tripId,
										@RequestParam(name = "tripCapacity", required = false) String tripCapacity,
										@RequestParam(name = "tripDifficulty", required = false) String tripDifficulty,
										@RequestParam(name = "tripPoints", required = false) String tripPoints) {
		
		UserEntity currentUser = userUtils.getUserByEmail(principal.getName());
		Trip tripToSave = getTripById(Integer.parseInt(tripId));

		if(actionType != null) {
			switch(actionType) {
				case "Organizeaza ascensiunea": {				
					organiseTrip(tripToSave, tripCapacity, tripDifficulty, tripPoints, currentUser);
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
					
					if(isCapacityValid(tripCapacity) && isDifficultyValid(tripDifficulty) && arePointsValid(tripPoints)) {
						organiseTrip(tripToSave, tripCapacity, tripDifficulty, tripPoints, currentUser);
						return "redirect:/all-trips";
					}				
					break;
				}
				case "Renunta": {
					return "redirect:/suggested-trips";
				}
			}
		}
		loadAllTrips(tripViewModel, model);
		return "views/guide/suggestedTripsView";
	}
	
	private void loadAllTrips(TripViewModel tripViewModel, Model model) {
		tripViewModel.setTripsDTO(getAllSuggestedTripsDTO());
		model.addAttribute("tripViewModel", tripViewModel);
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

	private List<TripDTO> getAllSuggestedTripsDTO(){
		List<TripDTO> tripsDTO = new ArrayList<>();
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getStatus().equals("Suggested")) {
				tripsDTO.add(conversion.convertFromTripToTripDTO(trip));
			}
		}
		return tripsDTO;
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
