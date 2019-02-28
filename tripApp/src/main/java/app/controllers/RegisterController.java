package app.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.constants.Cities;
import app.entities.User;
import app.repositories.UserRepository;
import app.validators.RegisterValidator;

@Controller
public class RegisterController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("allCities", this.getAllCities());
		
		
		
		return "views/register";
	}
	
	@PostMapping("/register")
	public String registerUser(User user, Model model,
							   @RequestParam(value = "firstName", required = false) String firstName,
							   @RequestParam(value = "lastName", required = false) String lastName,
							   @RequestParam(value = "email", required = false) String email,
							   @RequestParam(value = "password", required = false) String password,
							   @RequestParam(value = "city", required = false) String city,
							   @RequestParam(value = "birthDate", required = false) String birthDate) {	
		
		model.addAttribute("allCities", this.getAllCities());
		
		if(this.isFormValid(firstName, lastName, email, password, birthDate, city, model)) {
			userRepository.save(new User(firstName, lastName, birthDate, city, email, password));
			return "views/login";
		}		
		return "views/register";
	}
	
	private List<String> getAllCities() {
		List<Cities> citiesFromEnum = new ArrayList<Cities>(Arrays.asList(Cities.values()));
		List<String> cities = new ArrayList<String>();
		
		for(Cities city : citiesFromEnum) {
			cities.add(city.toString());
		}	
		return cities;
	}
	
	private boolean isFormValid(String firstName, String lastName, String email, String password, String birthDate, String city, Model model) {
		RegisterValidator validator = new RegisterValidator();
		
		boolean isFirstNameValid = true;
		boolean isLastNameValid = true;
		boolean isPasswordValid = true;
		boolean isEmailValid = true;
		boolean isDateValid = true;
		
		if(!validator.isNameValid(firstName)) {
			isFirstNameValid = false;
			model.addAttribute("isFirstNameValid", isFirstNameValid);
		}
		
		if(!validator.isNameValid(lastName)) {
			isLastNameValid = false;
			model.addAttribute("isLastNameValid", isLastNameValid);			
		}
		
		if(!validator.isPasswordValid(password)) {
			isPasswordValid = false;
			model.addAttribute("isPasswordValid", isPasswordValid);		
		}
		
		if(!validator.isEmailValid(email)) {
			isEmailValid = false;
			model.addAttribute("isEmailValid", isEmailValid);		
		}
		
		if(!validator.isDateValid(birthDate)) {
			isDateValid = false;
			model.addAttribute("isDateValid", isDateValid);		
		}
				
		if(isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid && isDateValid) {		
			return true;
		}
		return false;
	}
}
