package app.controllers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import app.utils.Conversion;
import app.utils.TripUtils;
import app.utils.api.WeatherApi;

@Controller
public class IndexController {
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private Conversion conversion;
	
	@GetMapping("/")
	public String getIndexPage(Model model, TripViewModel tripViewModel, HttpSession session) throws UnknownHostException {
		// loads the 4 active trips for guest main page
		if(this.getTop4TripsDTO().size() > 0) {
			tripViewModel.setTripsDTO(this.getTop4TripsDTO());
		}		
		model.addAttribute("tripViewModel", tripViewModel);	
		
		// loads most popular 4 trip locations (to do)
		
		// loads article (to do)
			
		Map<String, String> weatherData = WeatherApi.getWeatherData();
		model.addAttribute("temperature", weatherData.get("temperature"));
		model.addAttribute("humidity", weatherData.get("humidity"));
		model.addAttribute("pressure", weatherData.get("pressure"));
		model.addAttribute("windSpeed", weatherData.get("windSpeed"));
		model.addAttribute("weather", weatherData.get("weather"));
		model.addAttribute("clouds", weatherData.get("clouds"));

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
				TripDTO tripDTO = conversion.convertFromTripToTripDTO(trip);
				top4TripsDTO.add(tripDTO);			
			}
			if(top4TripsDTO.size() == 4) {
				break;
			}
		}
		return top4TripsDTO;
	}
}
