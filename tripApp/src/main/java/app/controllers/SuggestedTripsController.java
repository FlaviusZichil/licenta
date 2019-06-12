package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dto.TripDTO;
import app.entities.Trip;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.utils.Conversion;

@Controller
public class SuggestedTripsController {
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private Conversion conversion;

	@GetMapping("/suggested-trips")
	public String getSuggestedTripsPage(Model model, TripViewModel tripViewModel) {
		tripViewModel.setTripsDTO(getAllSuggestedTripsDTO());
		model.addAttribute("tripViewModel", tripViewModel);
		return "views/guide/suggestedTripsView";
	}
	
	@PostMapping("/suggested-trips")
	public String suggestedTripsActions(Model model) {
		return "views/guide/suggestedTripsView";
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
}
