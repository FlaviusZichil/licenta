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

import app.dto.CityDTO;
import app.dto.GuideDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.TripDTO;
import app.entities.Peak;
import app.entities.Trip;
import app.models.TripViewModel;
import app.repositories.TripRepository;

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
	public String indexPage(Model model,
							@RequestParam(value = "submit", required = false) String actionType) {
		return "";
	}
	
	// gets 4 trips to display on guest main page
	private List<TripDTO> getTop4TripsDTO(){
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<Trip> trips = this.convertFromIterableToList(allTrips);
		List<TripDTO> top4TripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : trips) {
			if(trip.getStatus().equals("Active")) {			
				PeakDTO peakDTO = convertPeakToPeakDTO(trip.getPeak());
				GuideDTO guideDTO = new GuideDTO(trip.getGuide().getId(),trip.getGuide().getUser(), trip.getGuide().getYearsOfExperience(), trip.getGuide().getPhoneNumber());
				top4TripsDTO.add(this.convertTripToTripDTO(trip, peakDTO, guideDTO));
			}
			if(top4TripsDTO.size() == 4) {
				break;
			}
		}
		return top4TripsDTO;
	}
	
	private PeakDTO convertPeakToPeakDTO(Peak peak) {
		MountainDTO mountainDTO = new MountainDTO(peak.getMountain().getMountainName());
		CityDTO cityDTO = new CityDTO(peak.getCity().getName());
		PeakDTO peakDTO = new PeakDTO(peak.getId(), peak.getPeakName(), peak.getAltitude(), cityDTO, mountainDTO, peak.getTrips());
		return peakDTO;
	}
	
	// mai trebuie adaugat ca param si routeDTO
	private TripDTO convertTripToTripDTO(Trip trip, PeakDTO peakDTO, GuideDTO guideDTO) {
		TripDTO tripDTO = new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
									  trip.getDifficulty(), trip.getUsers(), trip.getRoute(), peakDTO, guideDTO);
		
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
