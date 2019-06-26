package app.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import app.entities.Trip;
import app.entities.UserEntity;
import app.repositories.TripRepository;
import app.repositories.UserRepository;

@Controller
public class AdminStatisticsController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TripRepository tripRepository;

	@GetMapping("/statistics")
	public String getStatisticsPage(Model model) {
		List<Integer> ages = getAgesFromUsers();
		List<Integer> experiences = getExperiencesFromUsers();
		List<Integer> roles = getRolesFromUsers();
		List<Integer> statuses = getStatusesFromUsers();
		List<Integer> trips = getTripsStatuses();
		List<Integer> monthsForCurrentYear = getTripByMonthsAndYear(String.valueOf(LocalDate.now().getYear()));
		List<Integer> monthsForLastYear = getTripByMonthsAndYear(String.valueOf(LocalDate.now().getYear() - 1));
		List<Integer> newUsersForCurrentYear = getNewUsersByMonthsAndYear(String.valueOf(LocalDate.now().getYear()));
		List<Integer> newUsersForLastYear = getNewUsersByMonthsAndYear(String.valueOf(LocalDate.now().getYear() - 1));
		List<String> guidesNames = new ArrayList<>(getTripsForGuides().keySet());
		List<Integer> guidesTrips = new ArrayList<>(getTripsForGuides().values());
		
		model.addAttribute("ages", ages);
		model.addAttribute("experiences", experiences);
		model.addAttribute("roles", roles);
		model.addAttribute("statuses", statuses);
		model.addAttribute("trips", trips);
		model.addAttribute("monthsForCurrentYear", monthsForCurrentYear);
		model.addAttribute("monthsForLastYear", monthsForLastYear);
		model.addAttribute("newUsersForCurrentYear", newUsersForCurrentYear);
		model.addAttribute("newUsersForLastYear", newUsersForLastYear);
		model.addAttribute("currentYear", String.valueOf(LocalDate.now().getYear()));
		model.addAttribute("lastYear", String.valueOf(LocalDate.now().getYear() - 1));
		model.addAttribute("guidesNames", guidesNames);
		model.addAttribute("guidesTrips", guidesTrips);
	
		return "views/admin/statisticsView";
	}

	@PostMapping("/statistics")
	public String statisticsActions(Model model) {
		return "views/admin/statisticsView";
	}
	
	private void updateList(List<Integer> list, Integer index) {
		Integer newValue = list.get(index) + 1;
		list.set(index, newValue);
	}
	
	private List<Integer> getAgesFromUsers(){
		List<Integer> ages = Arrays.asList(0, 0, 0, 0);	
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_STAFF")) {
				if(getAgeFromBirthDate(user.getBirthDate()) < 20) {
					updateList(ages, 0);
				}
				if(getAgeFromBirthDate(user.getBirthDate()) >= 20 && getAgeFromBirthDate(user.getBirthDate()) < 30) {
					updateList(ages, 1);
				}
				if(getAgeFromBirthDate(user.getBirthDate()) >= 30 && getAgeFromBirthDate(user.getBirthDate()) < 50) {
					updateList(ages, 2);
				}
				if(getAgeFromBirthDate(user.getBirthDate()) > 50) {
					updateList(ages, 3);
				}
			}		
		}
		return ages;
	}
	
	private Integer getAgeFromBirthDate(String birthDate) {
		LocalDate birth = LocalDate.parse(birthDate);
		Period period = Period.between(birth, LocalDate.now());
	    Integer years = period.getYears();
	    return years;
	}
	
	private List<Integer> getExperiencesFromUsers() {
		List<Integer> experiences = Arrays.asList(0, 0, 0);	
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_STAFF")) {
				if(user.getExperience() != null && user.getExperience().equals("Incepator")) {
					updateList(experiences, 0);
				}
				if(user.getExperience() != null && user.getExperience().equals("Mediu")) {
					updateList(experiences, 1);
				}
				if(user.getExperience() != null && user.getExperience().equals("Avansat")) {
					updateList(experiences, 2);
				}
			}		
		}
		return experiences;
	}
	
	private List<Integer> getRolesFromUsers(){
		List<Integer> roles = Arrays.asList(0, 0, 0);	
		for(UserEntity user : userRepository.findAll()) {
			if(!user.getRole().getName().equals("ROLE_ADMIN") && !user.isBlocked()) {
				switch(user.getRole().getName()) {
					case "ROLE_USER": {
						updateList(roles, 0);
						break;
					}
					case "ROLE_STAFF": {
						updateList(roles, 1);
						break;
					}
					case "ROLE_GUIDE": {
						updateList(roles, 2);
						break;
					}	
				}
			}		
		}
		return roles;
	}
	
	private List<Integer> getStatusesFromUsers(){
		List<Integer> statuses = Arrays.asList(0, 0);	
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_STAFF")) {
				if(!user.isBlocked()) {
					updateList(statuses, 0);
				}
				else {
					updateList(statuses, 1);
				}
			}		
		}
		return statuses;
	}
	
	private List<Integer> getTripsStatuses(){
		List<Integer> statuses = Arrays.asList(0, 0);	
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getStatus().equals("Active")) {
				updateList(statuses, 0);
			}
			else {
				updateList(statuses, 1);
			}			
		}
		return statuses;
	}
	
	private List<Integer> getTripByMonthsAndYear(String year){
		List<Integer> months = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		for(Trip trip : tripRepository.findAll()) {
			LocalDate startDate = LocalDate.parse(trip.getStartDate());
			if(String.valueOf(startDate.getYear()).equals(year)) {
				Integer newValue = months.get(startDate.getMonthValue() - 1) + 1;
				months.set(startDate.getMonthValue() - 1, newValue);
			}			
		}
		return months;
	}
	
	private List<Integer> getNewUsersByMonthsAndYear(String year){
		List<Integer> months = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_STAFF")) {
				LocalDate registerDate = LocalDate.parse(user.getRegister().getRegisterDate());
				if(String.valueOf(registerDate.getYear()).equals(year)) {
					Integer newValue = months.get(registerDate.getMonthValue() - 1) + 1;
					months.set(registerDate.getMonthValue() - 1, newValue);
				}
			}					
		}
		return months;
	}
	
	private Map<String, Integer> getTripsForGuides(){
		Map<String, Integer> trips = createNameTripsMap();
		for (Map.Entry<String, Integer> entry : trips.entrySet()) {
		    for(Trip trip : tripRepository.findAll()) {
		    	if(!trip.getStatus().equals("Suggested")) {
		    		if(entry.getKey().equals(trip.getGuide().getUser().getLastName() + " " + trip.getGuide().getUser().getFirstName())) {
			    		Integer newValue = entry.getValue() + 1;
			    		trips.put(entry.getKey(), newValue);
			    	}
		    	}	    	
		    }
		}
		return trips;
	}
	
	private Map<String, Integer> createNameTripsMap() {
		Map<String, Integer> trips = new LinkedHashMap<>();
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_GUIDE")) {
				trips.put(user.getLastName() + " " + user.getFirstName(), 0);
			}
		}
		return trips;
	}
}
