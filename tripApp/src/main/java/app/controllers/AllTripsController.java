package app.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import app.dto.TripDTO;
import app.entities.City;
import app.entities.Trip;
import app.entities.UserEntity;
import app.entities.UserTrip;
import app.models.TripViewModel;
import app.repositories.TripRepository;
import app.repositories.UserTripRepository;
import app.utils.Conversion;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class AllTripsController {
	@Autowired
	private UserTripRepository userTripRepository;
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private Conversion conversion;

	@SessionScope
	@GetMapping("/all-trips")
	public String getAllTripsPage(Model model, TripViewModel tripViewModel, HttpSession session, Principal principal) {
		if (this.getAllTripsDTOAvailableForUser(principal).isEmpty()) {
			model.addAttribute("areTripsAvailable", false);
		} else {
			tripViewModel.setTripsDTO(this.getAllTripsDTOAvailableForUser(principal));
			model.addAttribute("tripViewModel", tripViewModel);
		}
		
		if(principal != null) {
			UserEntity user = userUtils.getUserByEmail(principal.getName());

			if(user.getRole().getName().equals("ROLE_GUIDE") && TripUtils.getNumberOfFinishedTripsWithActiveStatusForGuide(user.getGuide()) > 0) {
				model.addAttribute("guideHasUnfinishedTrips", true);
			}
		}
		return "views/all/allTrips";
	}

	@PostMapping("/all-trips")
	public String allTripsActions(Model model, TripViewModel tripViewModel, Principal principal,
			@RequestParam(name = "month", required = false) String month,
			@RequestParam(name = "difficulty", required = false) String difficulty,
			@RequestParam(name = "distance", required = false) String distance) throws ParseException {

		if (month == null && difficulty == null && distance == null) {
			return "redirect:/all-trips";
		}
		
		if (month != null || difficulty != null || distance != null) {
			model.addAttribute("filterWasApplied", true);
		}

		List<String> difficulties = new ArrayList<>();
		List<String> months = new ArrayList<>();
		List<TripDTO> tripsDTO = new ArrayList<>();
		
		if (difficulty != null) {
			difficulties = new ArrayList<>(Arrays.asList(difficulty.split(",")));
		}

		if (month != null) {
			months = new ArrayList<>(Arrays.asList(month.split(",")));
		}

		tripsDTO = this.applyFiltersOnList(this.getAllTripsDTOAvailableForUser(principal), months, difficulties);
		
		// tests if only distance filter is applied
		if(distance != null && month == null && difficulty == null) {
			tripsDTO = this.getAllTripsDTOAvailableForUser(principal);
			tripViewModel.setTripsDTO(sortTripsByDistance(principal, tripsDTO));
			model.addAttribute("tripViewModel", tripViewModel);
		}

		if (tripsDTO.isEmpty()) {
			model.addAttribute("areResultsFoundForSelectedFilters", false);
		} else {
			if(distance != null) {
				tripViewModel.setTripsDTO(sortTripsByDistance(principal, tripsDTO));
				model.addAttribute("tripViewModel", tripViewModel);
			}
			else {
				tripViewModel.setTripsDTO(tripsDTO);
				model.addAttribute("tripViewModel", tripViewModel);
			}
		}
		return "views/all/allTrips";
	}
	
	public List<TripDTO> applyFiltersOnList(List<TripDTO> tripsDTO, List<String> months, List<String> difficulties) {
		List<TripDTO> tripsDTOAfterFilters = new ArrayList<>(); 
		for (TripDTO tripDTO : tripsDTO) {
			LocalDate startDate = LocalDate.parse(tripDTO.getStartDate());
			
			if(months.contains(startDate.getMonth().toString()) && difficulties.size() > 0 && !difficulties.contains(tripDTO.getRouteDTO().getDifficulty())) {
				continue;
			}

			if (months.contains(startDate.getMonth().toString()) && difficulties.contains(tripDTO.getRouteDTO().getDifficulty())) {
				tripsDTOAfterFilters.add(tripDTO);
				continue;
			}
			
			if(months.contains(startDate.getMonth().toString()) && difficulties.size() == 0) {
				tripsDTOAfterFilters.add(tripDTO);
				continue;
			}
			
			if(difficulties.contains(tripDTO.getRouteDTO().getDifficulty()) && months.size() == 0) {
				tripsDTOAfterFilters.add(tripDTO);
				continue;
			}
		}
		
		return tripsDTOAfterFilters;
	}

	private List<TripDTO> getAllTripsDTOAvailableForUser(Principal principal) {
		UserEntity currentUser = userUtils.getUserByEmail(principal.getName());
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();

		for (Trip trip : tripRepository.findAll()) {
			if (trip.getStatus().equals("Active") && getUserTrip(currentUser, trip) == null) {				
				TripDTO tripDTO = conversion.convertFromTripToTripDTO(trip);
				currentUserTripsDTO.add(tripDTO);
			}
		}
		return currentUserTripsDTO;
	}
	
	private UserTrip getUserTrip(UserEntity user, Trip trip) {
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getTrip().equals(trip) && userTrip.getUser().equals(user)) {
				return userTrip;
			}
		}
		return null;
	}
	
	private Double calculateDistanceBetweenTwoCities(City firstCity, City secondCity) {
		if ((firstCity.getLatitude() == secondCity.getLatitude()) && (firstCity.getLongitude() == secondCity.getLongitude())) {
			return 0.0;
		}
		else {
			double theta = firstCity.getLongitude() - secondCity.getLongitude();
			double distance = Math.sin(Math.toRadians(firstCity.getLatitude())) * Math.sin(Math.toRadians(secondCity.getLatitude())) + 
							  Math.cos(Math.toRadians(firstCity.getLatitude())) * Math.cos(Math.toRadians(secondCity.getLatitude())) * 
							  Math.cos(Math.toRadians(theta));
			distance = Math.acos(distance);
			distance = Math.toDegrees(distance);
			distance = distance * 60 * 1.1515;
			distance = distance * 1.609344;

			return distance;
		}
	}
	
	private List<TripDTO> sortTripsByDistance(Principal principal, List<TripDTO> tripsDTO){
		// trebuie luate doar cele active
		final UserEntity user = userUtils.getUserByEmail(principal.getName());	
		
		Collections.sort(tripsDTO, new Comparator<TripDTO>(){
			   @Override
			   public int compare(TripDTO firstTripDTO, TripDTO secondTripDTO) {			   
				   City firstTripCity = convertFromCityDTOToCity(firstTripDTO.getPeakDTO().getCityDTO());
				   City secondTripCity = convertFromCityDTOToCity(secondTripDTO.getPeakDTO().getCityDTO());		
				   
				   Double firstDistance = calculateDistanceBetweenTwoCities(user.getCity(), firstTripCity);
				   Double secondDistance = calculateDistanceBetweenTwoCities(user.getCity(), secondTripCity);
				   
				   if(firstDistance > secondDistance) {
					   return 1;
				   }
				   
				   if(firstDistance < secondDistance) {
					   return -1;
				   }
				   return 0;
			     }
			 });
		
		return tripsDTO;
	}
	
	private City convertFromCityDTOToCity(CityDTO cityDTO) {
		City firstTripCity = new City(cityDTO.getName(), cityDTO.getLatitude(), cityDTO.getLongitude());
		return firstTripCity;
	}
}
