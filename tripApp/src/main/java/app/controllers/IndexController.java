package app.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
				
		tripViewModel.setTripsDTO(this.convertTripstoTripsDTO());
		model.addAttribute("tripViewModel", tripViewModel);	

		return "views/index.html";
	}
	
	private List<TripDTO> convertTripstoTripsDTO(){
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<TripDTO> allTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : allTrips) {
			allTripsDTO.add(new TripDTO(trip));
		}
		return allTripsDTO;
	}
}
