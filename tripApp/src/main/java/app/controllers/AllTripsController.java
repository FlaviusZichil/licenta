package app.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import app.dto.CityDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.repositories.UserRepository;

@Controller
public class AllTripsController {

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@SessionScope
	@GetMapping("/all-trips")
	public String getAllTripsPage(Model model, TripViewModel tripViewModel, HttpSession session, Principal principal) {	
		if(this.getAllTripsDTOAvailableForUser(principal).isEmpty()) {
			model.addAttribute("areTripsAvailable", false);
		}else {
			tripViewModel.setTripsDTO(this.getAllTripsDTOAvailableForUser(principal));
			model.addAttribute("tripViewModel", tripViewModel);	
		}		
		return "views/all/allTrips";
	}
	
	@PostMapping("/all-trips")
	public String allTripsActions(Model model, TripViewModel tripViewModel, Principal principal,
								  @RequestParam(name = "groupOfDefaultRadios", required = false) String month) throws ParseException {
	
		if(month == null) {
			return "redirect:/all-trips";
		}
		
		List<TripDTO> tripsDTO = new ArrayList<>();
		
		for(TripDTO tripDTO : this.getAllTripsDTOAvailableForUser(principal)) {
			LocalDate startDate = LocalDate.parse(tripDTO.getStartDate());
			
			if(startDate.getMonth().toString().equals(month)) {
				tripsDTO.add(tripDTO);
			}
		}
			
		if(tripsDTO.isEmpty()) {
			model.addAttribute("areTripsAvailable", false);
		}else {
			tripViewModel.setTripsDTO(tripsDTO);
			model.addAttribute("tripViewModel", tripViewModel);	
		}	

		return "views/all/allTrips";
	}
	
	private List<TripDTO> getAllTripsDTOAvailableForUser(Principal principal){
		Iterable<Trip> allTrips = tripRepository.findAll();
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : allTrips) {
			if(trip.getStatus().equals("Active") && !currentUserTrips.contains(trip)) {
				CityDTO cityDTO = new CityDTO(trip.getPeak().getCity().getName());
				MountainDTO mountainDTO = new MountainDTO(trip.getPeak().getMountain().getMountainName());
				PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(), trip.getPeak().getAltitude(), cityDTO, 
											  mountainDTO, trip.getPeak().getTrips());
				
				currentUserTripsDTO.add(new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
						trip.getDifficulty(), trip.getUsers(), trip.getRoute(), peakDTO));
			}			
		}
		return currentUserTripsDTO;
	}
	
	private UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();
		
		for(UserEntity user : users) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
