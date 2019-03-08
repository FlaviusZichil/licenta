package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.TripDTO;
import app.entities.Trip;
import app.models.TripDetailsViewModel;
import app.repositories.TripRepository;

@Controller
public class TripDetailsController {

	@Autowired
	private TripRepository tripRepository;
	
	@GetMapping("/trip-details")
	public String getTripDetails(Model model, TripDetailsViewModel tripDetailsViewModel,
								@RequestParam(value = "trip", required = false) String tripId) {
		
		tripDetailsViewModel.setTripDTO(this.getTripDTOById(Integer.parseInt(tripId)));
		model.addAttribute("tripDetailsViewModel", tripDetailsViewModel);
	
		return "views/tripDetails";
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
}
