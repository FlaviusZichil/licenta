package app.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.entities.UserEntity;
import app.repositories.UserRepository;

@Controller
public class AdminStatisticsController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/statistics")
	public String getStatisticsPage(Model model) {
		List<Integer> ages = getAgesFromUsers();
		List<Integer> experiences = getExperiencesFromUsers();
		model.addAttribute("ages", ages);
		model.addAttribute("experiences", experiences);
		return "views/admin/statisticsView";
	}
	
	@PostMapping("/statistics")
	public String statisticsActions(Model model) {
		return "views/admin/statisticsView";
	}
	
	private List<Integer> getAgesFromUsers(){
		List<Integer> ages = Arrays.asList(0, 0, 0, 0);	
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER") || user.getRole().getName().equals("ROLE_STAFF")) {
				if(getAgeFromBirthDate(user.getBirthDate()) < 20) {
					Integer newValue = ages.get(0) + 1;
					ages.set(0, newValue);
				}
				if(getAgeFromBirthDate(user.getBirthDate()) >= 20 && getAgeFromBirthDate(user.getBirthDate()) < 30) {
					Integer newValue = ages.get(1) + 1;
					ages.set(1, newValue);
				}
				if(getAgeFromBirthDate(user.getBirthDate()) >= 30 && getAgeFromBirthDate(user.getBirthDate()) < 50) {
					Integer newValue = ages.get(2) + 1;
					ages.set(2, newValue);
				}
				if(getAgeFromBirthDate(user.getBirthDate()) > 50) {
					Integer newValue = ages.get(3) + 1;
					ages.set(3, newValue);
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
					Integer newValue = experiences.get(0) + 1;
					experiences.set(0, newValue);
				}
				if(user.getExperience() != null && user.getExperience().equals("Mediu")) {
					Integer newValue = experiences.get(1) + 1;
					experiences.set(1, newValue);
				}
				if(user.getExperience() != null && user.getExperience().equals("Avansat")) {
					Integer newValue = experiences.get(2) + 1;
					experiences.set(2, newValue);
				}
			}		
		}
		return experiences;
	}
}
