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
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.TripDTO;
import app.entities.City;
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

		if (month == null && difficulty == null) {
			return "redirect:/all-trips";
		}
		
		if (month != null || difficulty != null) {
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

		for (TripDTO tripDTO : this.getAllTripsDTOAvailableForUser(principal)) {
			LocalDate startDate = LocalDate.parse(tripDTO.getStartDate());
			
			if(months.contains(startDate.getMonth().toString()) && difficulties.size() > 0 && !difficulties.contains(tripDTO.getDifficulty())) {
				continue;
			}

			if (months.contains(startDate.getMonth().toString()) && difficulties.contains(tripDTO.getDifficulty())) {
				tripsDTO.add(tripDTO);
				continue;
			}
			
			if(months.contains(startDate.getMonth().toString()) && difficulties.size() == 0) {
				tripsDTO.add(tripDTO);
				continue;
			}
			
			if(difficulties.contains(tripDTO.getDifficulty()) && months.size() == 0) {
				tripsDTO.add(tripDTO);
				continue;
			}
		}

		if (tripsDTO.isEmpty()) {
			model.addAttribute("isFilterApplied", false);
		} else {
			tripViewModel.setTripsDTO(tripsDTO);
//			model.addAttribute("tripViewModel", tripViewModel);
			tripViewModel.setTripsDTO(sortTripsByDistance(principal, tripsDTO));

		}
		
		if(distance != null && month == null && difficulty == null) {
			System.out.println(distance);
			System.out.println(tripsDTO.toString());
			System.out.println(sortTripsByDistance(principal, tripsDTO).toString());
			tripViewModel.setTripsDTO(sortTripsByDistance(principal, tripsDTO));
			model.addAttribute("tripViewModel", tripViewModel);
		}

		return "views/all/allTrips";
	}

	private List<TripDTO> getAllTripsDTOAvailableForUser(Principal principal) {
		Iterable<Trip> allTrips = tripRepository.findAll();
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();

		for (Trip trip : allTrips) {
			if (trip.getStatus().equals("Active") && !currentUserTrips.contains(trip)) {
				CityDTO cityDTO = new CityDTO(trip.getPeak().getCity().getName());
				MountainDTO mountainDTO = new MountainDTO(trip.getPeak().getMountain().getMountainName());
				PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(),
						trip.getPeak().getAltitude(), cityDTO, mountainDTO, trip.getPeak().getTrips());

				currentUserTripsDTO.add(new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(),
						trip.getEndDate(), trip.getStatus(), trip.getPoints(), trip.getDifficulty(), trip.getUsers(),
						trip.getRoute(), peakDTO));
			}
		}
		return currentUserTripsDTO;
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
				   
				   System.out.println("CITY DTO: " + firstTripDTO.getPeakDTO().getCityDTO());
				   
				   City firstTripCity = convertFromCityDTOToCity(firstTripDTO.getPeakDTO().getCityDTO());
				   City secondTripCity = convertFromCityDTOToCity(secondTripDTO.getPeakDTO().getCityDTO());
				   
				   System.out.println("FIRST_TRIP: " + firstTripCity.toString());
				   
				   System.out.println("LONGITUDE1" + firstTripCity.getLongitude());
				   System.out.println("LONGITUDE2" + secondTripCity.getLongitude());
				   
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
