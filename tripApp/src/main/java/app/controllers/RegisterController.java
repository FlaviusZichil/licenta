package app.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entities.City;
import app.entities.PromoCode;
import app.entities.Register;
import app.entities.Role;
import app.entities.UserEntity;
import app.repositories.CityRepository;
import app.repositories.RegisterRepository;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import app.utils.UserUtils;
import app.validators.RegisterValidator;

@Controller
public class RegisterController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private UserUtils userUtils;

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("allCities", this.getAllCities());
		return "views/all/register";
	}

	@PostMapping("/register")
	public String registerUser(Model model, @RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "birthDate", required = false) String birthDate) {

		RegisterValidator validator = new RegisterValidator();
		model.addAttribute("allCities", this.getAllCities());

		if (this.isFormValid(firstName, lastName, email, password, birthDate, model)) {
			UserEntity user = new UserEntity(validator.formatNameProperly(firstName), validator.formatNameProperly(lastName), birthDate, email, password);		
			Role role = this.getRoleByName("ROLE_USER");
			PromoCode promoCode = new PromoCode(this.generatePromoCode(), "Active");		
			City cityForUser = this.getCityByName(city);
			
			user.setPromoCode(promoCode);
			user.setRole(role);
			user.setCity(cityForUser);
			user.setPoints("25");
			userRepository.save(user);
			
			addRegisterDataForUser(user.getEmail());
			return "views/all/login";
		}
		return "views/all/register";
	}
	
	private void addRegisterDataForUser(String email) {
		UserEntity user = userUtils.getUserByEmail(email);
		Register registerData = new Register(user, LocalDate.now().toString());
		registerRepository.save(registerData);
	}

	private City getCityByName(String cityName) {
		Iterable<City> allCities = cityRepository.findAll();
		
		for(City city : allCities) {
			if(city.getName().trim().toUpperCase().equals(cityName.trim().toUpperCase())) {
				return city;
			}
		}
		return null;
	}
	
	private List<String> getAllCities() {
		List<String> cities = new ArrayList<String>();
		Iterable<City> citiesFromDatabase = cityRepository.findAll();
		
		for(City city : citiesFromDatabase) {
			cities.add(city.getName().trim().toUpperCase());
		}
		
		Collections.sort(cities);		
		return cities;
	}

	private boolean isFormValid(String firstName, String lastName, String email, String password, String birthDate, Model model) {
		RegisterValidator validator = new RegisterValidator();

		boolean isFirstNameValid = true;
		boolean isLastNameValid = true;
		boolean isPasswordValid = true;
		boolean isEmailValid = true;
		boolean isDateValid = true;

		if (!validator.isNameValid(firstName)) {
			isFirstNameValid = false;
			model.addAttribute("isFirstNameValid", isFirstNameValid);
		}

		if (!validator.isNameValid(lastName)) {
			isLastNameValid = false;
			model.addAttribute("isLastNameValid", isLastNameValid);
		}

		if (!validator.isPasswordValid(password)) {
			isPasswordValid = false;
			model.addAttribute("isPasswordValid", isPasswordValid);
		}

		if (!validator.isEmailValid(email) || this.isEmailTaken(email)) {
			isEmailValid = false;
			model.addAttribute("isEmailValid", isEmailValid);
		}

		if (!validator.isDateValid(birthDate)) {
			isDateValid = false;
			model.addAttribute("isDateValid", isDateValid);
		}

		if (isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid && isDateValid) {
			return true;
		}
		return false;
	}

	private boolean isEmailTaken(String email) {
		Iterable<UserEntity> allUsers = userRepository.findAll();
		for (UserEntity user : allUsers) {
			if (user.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}

	private Role getRoleByName(String roleName) {
		Iterable<Role> allRoles = roleRepository.findAll();

		for (Role role : allRoles) {
			if (role.getName().equals(roleName)) {
				return role;
			}
		}
		return null;
	}

	public String generatePromoCode() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 20) {
            int index = (int) (random.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
	}
}
