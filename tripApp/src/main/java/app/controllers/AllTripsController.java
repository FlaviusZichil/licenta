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
import app.dto.GuideDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.PointDTO;
import app.dto.RouteDTO;
import app.dto.RoutePointDTO;
import app.dto.TripDTO;
import app.entities.City;
import app.entities.Route;
import app.entities.RoutePoint;
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
		if (this.getAllTripsDTOAvailableForUser(principal).isEmpty()) {
			model.addAttribute("areTripsAvailable", false);
		} else {
			tripViewModel.setTripsDTO(this.getAllTripsDTOAvailableForUser(principal));
			model.addAttribute("tripViewModel", tripViewModel);
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
		Iterable<Trip> allTrips = tripRepository.findAll();
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();

		for (Trip trip : allTrips) {
			if (trip.getStatus().equals("Active") && !currentUserTrips.contains(trip)) {
				CityDTO cityDTO = new CityDTO(trip.getPeak().getCity().getName(), trip.getPeak().getCity().getLatitude(), trip.getPeak().getCity().getLongitude());
				MountainDTO mountainDTO = new MountainDTO(trip.getPeak().getMountain().getMountainName());
				PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(),
						trip.getPeak().getAltitude(), cityDTO, mountainDTO, trip.getPeak().getTrips());
				GuideDTO guideDTO = new GuideDTO(trip.getGuide().getId(), trip.getGuide().getUser(), trip.getGuide().getYearsOfExperience(), 
						trip.getGuide().getPhoneNumber());
				RouteDTO routeDTO = new RouteDTO(trip.getRoute().getId(), trip.getRoute().getDifficulty(), this.getRoutePointsDTOForTrip(trip));

				currentUserTripsDTO.add(new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(),trip.getEndDate(), trip.getStatus(), 
						                            trip.getPoints(), trip.getUsers(), routeDTO, peakDTO, guideDTO));
			}
		}
		return currentUserTripsDTO;
	}
	
	private List<RoutePointDTO> getRoutePointsDTOForTrip(Trip trip){
		List<RoutePointDTO> routePointsDTO = new ArrayList<>();
		
		Route route = trip.getRoute();
		List<RoutePoint> routePoints = route.getRoutePoints();
		
		for(RoutePoint routePoint : routePoints) {
			PointDTO pointDTO = new PointDTO(routePoint.getPoint().getId(), routePoint.getPoint().getPointName());
			RoutePointDTO routePointDTO = new RoutePointDTO(routePoint.getId(), routePoint.getOrder(), pointDTO);
			routePointsDTO.add(routePointDTO);
		}	
		return routePointsDTO;
	}

	private UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();

		for (UserEntity user : users) {
			if (user.getEmail().equals(email)) {
				return user;
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
		final UserEntity user = this.getUserByEmail(principal.getName());	
		
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
