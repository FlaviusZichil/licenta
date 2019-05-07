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
import app.utils.TripUtils;

@Controller
public class IndexController {
	
	@Autowired
	private TripRepository tripRepository;
	
	@GetMapping("/")
	public String getIndexPage(Model model, TripViewModel tripViewModel, HttpSession session) {
		// loads the 4 active trips for guest main page
		if(this.getTop4TripsDTO().size() > 0) {
			tripViewModel.setTripsDTO(this.getTop4TripsDTO());
		}		
		model.addAttribute("tripViewModel", tripViewModel);	
		
		// loads most popular 4 trip locations (to do)
		
		// loads article (to do)

		return "views/all/index.html";
	}
	
	@PostMapping("/")
	public String indexPage(Model model, @RequestParam(value = "submit", required = false) String actionType) {
		switch(actionType) {
			case "Participa":
			{
				return "redirect:/tombola";
			}
		}
		return "";
	}
	
	// gets 4 trips to display on guest main page
	private List<TripDTO> getTop4TripsDTO(){
		List<TripDTO> top4TripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getStatus().equals("Active")) {			
				TripDTO tripDTO = TripUtils.convertFromTripToTripDTO(trip);
				top4TripsDTO.add(tripDTO);			
			}
			if(top4TripsDTO.size() == 4) {
				break;
			}
		}
		return top4TripsDTO;
	}
}
