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

import app.entities.Tombola;
import app.entities.UserEntity;
import app.models.TombolaModel;
import app.repositories.TombolaRepository;
import app.repositories.UserRepository;
import app.utils.TripUtils;

@Controller
public class TombolaController {

	@Autowired
	private TombolaRepository tombolaRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/tombola")
	public String getTombola(Model model, Principal principal) {
		model.addAttribute("winners", this.getAllWinners());		
		return "views/all/tombola";
	}
	
	@PostMapping("/tombola")
	public String postTombola(Model model, Principal principal,
							  @RequestParam(name = "registerToTombola", required = false) String registerToTombolaAction) {
		
		model.addAttribute("winners", this.getAllWinners());
		UserEntity currentUser = this.getUserByEmail(principal.getName());
		LocalDate date = LocalDate.now();
		
		if(registerToTombolaAction != null) {
			if (!isUserAlreadyRegisteredForThisMonth(currentUser, date.getMonth().toString(), date.getYear())) {
				this.registerUserToTombola(currentUser, date);
				model.addAttribute("successfulRegistrationToTombola", true);
			} else{
				model.addAttribute("failRegistrationToTombola", true);
			}
		}
		
		return "views/all/tombola";
	}

	private List<TombolaModel> getAllWinners() {
		List<TombolaModel> winners = new ArrayList<>();

		for (String date : this.getAllDistinctMonths()) {
			TombolaModel tombolaModel = new TombolaModel();
			tombolaModel.setDate(this.translateMonth(date));
			for (Tombola registrationForGivenDate : this.getAllRegistrationsForDate(date.toString())) {
				if (registrationForGivenDate.getStatus().equals("first")) {
					tombolaModel.setFirstPlaceWinner(registrationForGivenDate.getUser());
				}

				if (registrationForGivenDate.getStatus().equals("second")) {
					tombolaModel.setSecondPlaceWinner(registrationForGivenDate.getUser());
				}

				if (registrationForGivenDate.getStatus().equals("third")) {
					tombolaModel.setThirdPlaceWinner(registrationForGivenDate.getUser());
				}
			}
			if(tombolaModel.getFirstPlaceWinner() != null && tombolaModel.getSecondPlaceWinner() != null && tombolaModel.getThirdPlaceWinner() != null) {
				winners.add(tombolaModel);
			}				
		}
		return winners;
	}

	private List<String> getAllDistinctMonths() {
		List<String> distinctMonths = new ArrayList<>();
		Iterable<Tombola> registrationsToTombola = tombolaRepository.findAll();
		List<Tombola> registrations = TripUtils.convertFromIterableToList(registrationsToTombola);

		for (Tombola registration : registrations) {
			LocalDate date = LocalDate.parse(registration.getDate());
			if (!distinctMonths.contains(date.getMonth().toString() + " " + date.getYear())) {
				distinctMonths.add(date.getMonth().toString() + " " + date.getYear());
			}
		}
		return distinctMonths;
	}

	private List<Tombola> getAllRegistrationsForDate(String date) {
		List<Tombola> registrationsForDate = new ArrayList<>();
		Iterable<Tombola> registrationsToTombola = tombolaRepository.findAll();
		List<Tombola> registrations = TripUtils.convertFromIterableToList(registrationsToTombola);

		for (Tombola registration : registrations) {
			LocalDate dateOfRegistration = LocalDate.parse(registration.getDate());
			String dateString = dateOfRegistration.getMonth().toString() + " " + dateOfRegistration.getYear();
			if (dateString.equals(date)) {
				registrationsForDate.add(registration);
			}
		}
		return registrationsForDate;
	}

	private String translateMonth(String month) {
		switch (month.substring(0, month.indexOf(" "))) {
			case "JANUARY": {
				return month.replace("JANUARY", "Ianuarie");
			}
			case "FEBRUARY": {
				return month.replace("FEBRUARY", "Februarie");
			}
			case "MARCH": {
				return month.replace("MARCH", "Martie");
			}
			case "APRIL": {
				return month.replace("APRIL", "Aprilie");
			}
			case "MAY": {
				return month.replace("MAY", "Mai");
			}
			case "JUNE": {
				return month.replace("JUNE", "Iunie");
			}
			case "JULY": {
				return month.replace("JULY", "Iulie");
			}
			case "AUGUST": {
				return month.replace("AUGUST", "August");
			}
			case "SEPTEMBER": {
				return month.replace("SEPTEMBER", "Septembrie");
			}
			case "OCTOBER": {
				return month.replace("OCTOBER", "Octombrie");
			}
			case "NOVEMBER": {
				return month.replace("NOVEMBER", "Noiembrie");
			}
			case "DECEMBER": {
				return month.replace("DECEMBER", "December");
			}
		}
		return null;
	}

	private boolean isUserAlreadyRegisteredForThisMonth(UserEntity user, String month, Integer year) {
		Iterable<Tombola> registrations = tombolaRepository.findAll();
		List<Tombola> registrationsToTombola = TripUtils.convertFromIterableToList(registrations);

		if (registrationsToTombola.size() != 0) {
			for (Tombola registration : registrations) {
				LocalDate date = LocalDate.parse(registration.getDate());
				if (registration.getUser().equals(user) && date.getMonth().toString().equals(month)
						&& date.getYear() == year) {
					return true;
				}
			}
		}
		return false;
	}
	
	

	private void registerUserToTombola(UserEntity user, LocalDate date) {
		if (Integer.parseInt(user.getPoints()) >= 25) {
			tombolaRepository.save(new Tombola(date.toString(), "not winner", user));
			Integer userPoints = Integer.parseInt(user.getPoints()) - 25;
			user.setPoints(userPoints.toString());
			userRepository.save(user);
		} else {
			System.out.println("not enough points");
		}
	}

	public UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();

		for (UserEntity user : users) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
