package app.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import app.dto.TombolaDTO;
import app.entities.Tombola;
import app.entities.UserEntity;
import app.models.TombolaViewModel;
import app.repositories.TombolaRepository;
import app.repositories.UserRepository;
import app.utils.UserUtils;

@Controller
public class TombolaController {

	@Autowired
	private TombolaRepository tombolaRepository;
	
	@Autowired
	private UserUtils userUtils;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/tombola")
	public String getTombola(Model model, Principal principal, TombolaViewModel tombolaViewModel) {	
		tombolaViewModel.setWinners(this.getAllWinners());
		model.addAttribute("tombolaViewModel", tombolaViewModel);
		return "views/all/tombola";
	}
	
	@PostMapping("/tombola")
	public String postTombola(Model model, Principal principal, TombolaViewModel tombolaViewModel,
							  @RequestParam(name = "registerToTombola", required = false) String registerToTombolaAction) {
		
		tombolaViewModel.setWinners(this.getAllWinners());
		model.addAttribute("tombolaViewModel", tombolaViewModel);
		UserEntity currentUser = userUtils.getUserByEmail(principal.getName());
		LocalDate date = LocalDate.now();
		
		if(registerToTombolaAction != null) {
			if (!isUserAlreadyRegisteredForThisMonth(currentUser, date.getMonth().toString(), date.getYear()) && Integer.parseInt(currentUser.getPoints()) >= 25) {
				this.registerUserToTombola(currentUser, date);
				model.addAttribute("successfulRegistrationToTombola", true);
			} else{
				model.addAttribute("failRegistrationToTombola", true);
			}
		}
		
		return "views/all/tombola";
	}

	private List<TombolaDTO> getAllWinners() {
		List<TombolaDTO> winners = new ArrayList<>();

		for (String date : this.getAllDistinctMonths()) {
			TombolaDTO tombolaDTO = new TombolaDTO();
			tombolaDTO.setDate(this.translateDate(date));
			
			for (Tombola registrationForGivenDate : this.getAllRegistrationsForDate(date.toString())) {
				UserEntity user = registrationForGivenDate.getUser();
				if (registrationForGivenDate.getStatus().equals("first")) {
					tombolaDTO.setFirstPlaceWinner(userUtils.convertFromUserToUserDTO(user));
				}

				if (registrationForGivenDate.getStatus().equals("second")) {
					tombolaDTO.setSecondPlaceWinner(userUtils.convertFromUserToUserDTO(user));
				}

				if (registrationForGivenDate.getStatus().equals("third")) {
					tombolaDTO.setThirdPlaceWinner(userUtils.convertFromUserToUserDTO(user));
				}
			}
			if(tombolaDTO.getFirstPlaceWinner() != null && tombolaDTO.getSecondPlaceWinner() != null && tombolaDTO.getThirdPlaceWinner() != null) {
				winners.add(tombolaDTO);
			}				
		}
		return winners;
	}

	private List<String> getAllDistinctMonths() {
		List<String> distinctMonths = new ArrayList<>();

		for (Tombola registration : tombolaRepository.findAll()) {
			LocalDate date = LocalDate.parse(registration.getDate());
			if (!distinctMonths.contains(date.getMonth().toString() + " " + date.getYear())) {
				distinctMonths.add(date.getMonth().toString() + " " + date.getYear());
			}
		}
		return distinctMonths;
	}

	private List<Tombola> getAllRegistrationsForDate(String date) {
		List<Tombola> registrationsForDate = new ArrayList<>();

		for (Tombola registration : tombolaRepository.findAll()) {
			LocalDate dateOfRegistration = LocalDate.parse(registration.getDate());
			String dateString = dateOfRegistration.getMonth().toString() + " " + dateOfRegistration.getYear();
			if (dateString.equals(date)) {
				registrationsForDate.add(registration);
			}
		}
		return registrationsForDate;
	}

	private String translateDate(String date) {
		switch (date.substring(0, date.indexOf(" "))) {
			case "JANUARY": {
				return date.replace("JANUARY", "Ianuarie");
			}
			case "FEBRUARY": {
				return date.replace("FEBRUARY", "Februarie");
			}
			case "MARCH": {
				return date.replace("MARCH", "Martie");
			}
			case "APRIL": {
				return date.replace("APRIL", "Aprilie");
			}
			case "MAY": {
				return date.replace("MAY", "Mai");
			}
			case "JUNE": {
				return date.replace("JUNE", "Iunie");
			}
			case "JULY": {
				return date.replace("JULY", "Iulie");
			}
			case "AUGUST": {
				return date.replace("AUGUST", "August");
			}
			case "SEPTEMBER": {
				return date.replace("SEPTEMBER", "Septembrie");
			}
			case "OCTOBER": {
				return date.replace("OCTOBER", "Octombrie");
			}
			case "NOVEMBER": {
				return date.replace("NOVEMBER", "Noiembrie");
			}
			case "DECEMBER": {
				return date.replace("DECEMBER", "December");
			}
		}
		return null;
	}

	private boolean isUserAlreadyRegisteredForThisMonth(UserEntity user, String month, Integer year) {
		if (((List<Tombola>) tombolaRepository.findAll()).size() != 0) {
			for (Tombola registration : tombolaRepository.findAll()) {
				LocalDate date = LocalDate.parse(registration.getDate());
				if (registration.getUser().getId() == user.getId() && date.getMonth().toString().equals(month)
						&& date.getYear() == year) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void registerUserToTombola(UserEntity user, LocalDate date) {
		tombolaRepository.save(new Tombola(date.toString(), "new", user));
		decreaseUserPoints(user);
	}
	
	private void decreaseUserPoints(UserEntity user) {
		Integer userPoints = Integer.parseInt(user.getPoints()) - 25;
		user.setPoints(userPoints.toString());
		userRepository.save(user);
	}
}
