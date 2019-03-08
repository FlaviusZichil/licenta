package app.controllers;

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
import app.models.TripViewModel;
import app.repositories.TripRepository;

@Controller
public class IndexController {
	
	@Autowired
	private TripRepository tripRepository;
	
	@GetMapping("/")
	public String getIndexPage(Model model, TripViewModel tripViewModel, HttpSession session) {
		// loads the 4 active trips from guest main page
		tripViewModel.setTripsDTO(this.getTop4TripsDTO());
		model.addAttribute("tripViewModel", tripViewModel);	
		
		// loads most popular 4 trip locations (to do)
		
		// loads article (to do)

		return "views/index.html";
	}
	
	@PostMapping("/")
	public String indexPage(Model model,
							@RequestParam(value = "submit", required = false) String actionType) {
		
		switch(actionType) {
			case "Inscrie-te": {
				
				break;
			}
			case "Citeste articolul": {
				
				break;
			}
		}
		return "";
	}
	
	private List<TripDTO> getAllTripsDTO(){
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<TripDTO> allTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : allTrips) {
			if(trip.getStatus().equals("Active")) {
				allTripsDTO.add(new TripDTO(trip));
			}			
		}
		return allTripsDTO;
	}
	// gets 4 trips to display on guest main page
	private List<TripDTO> getTop4TripsDTO(){
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<Trip> trips = this.convertFromIterableToList(allTrips);
		List<TripDTO> top4TripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : trips) {
			if(trip.getStatus().equals("Active")) {
				top4TripsDTO.add(new TripDTO(trip));
			}
			if(top4TripsDTO.size() == 4) {
				break;
			}
		}
		return top4TripsDTO;
	}
	
	private List<Trip> convertFromIterableToList(Iterable<Trip> allTrips){
		List<Trip> trips = new ArrayList<Trip>();
		
		for(Trip trip : allTrips) {
			trips.add(trip);
		}
		return trips;
	}
}
