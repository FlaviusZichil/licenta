package app.controllers;

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
import app.entities.Role;
import app.entities.UserEntity;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import app.validators.RegisterValidator;

@Controller
public class RegisterController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("allCities", this.getAllCities());
		return "views/register";
	}
	
	@PostMapping("/register")
	public String registerUser(Model model,
							   @RequestParam(value = "firstName", required = false) String firstName,
							   @RequestParam(value = "lastName", required = false) String lastName,
							   @RequestParam(value = "email", required = false) String email,
							   @RequestParam(value = "password", required = false) String password,
							   @RequestParam(value = "city", required = false) String city,
							   @RequestParam(value = "birthDate", required = false) String birthDate) {	
		
		RegisterValidator validator = new RegisterValidator();
		model.addAttribute("allCities", this.getAllCities());
				
		if(this.isFormValid(firstName, lastName, email, password, birthDate, city, model)) {
			List<Role> rolesForRegisteredUser = this.addRolesForRegisteredUser();
			
			UserEntity user = new UserEntity(validator.formatNameProperly(firstName), 
					   						 validator.formatNameProperly(lastName), birthDate, city, email, password);
			
			user.setRoles(rolesForRegisteredUser);
			
			System.out.println(user.toString());
			
			userRepository.save(user);
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
		
		if(!validator.isEmailValid(email) || this.isEmailTaken(email)) {
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
	
	private boolean isEmailTaken(String email) {
		Iterable<UserEntity> allUsers = userRepository.findAll();
		for(UserEntity user : allUsers) {
			if(user.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
	
	private UserEntity findUserByEmail(String email) {
		Iterable<UserEntity> allUsers = userRepository.findAll();
		
		for(UserEntity user : allUsers) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
	
	private Role getRoleByName(String roleName) {
		Iterable<Role> allRoles = roleRepository.findAll();
		
		for(Role role : allRoles) {
			if(role.getName().equals(roleName)) {
				return role;
			}
		}
		return null;
	}
	
	private List<Role> addRolesForRegisteredUser(){
		List<Role> rolesForRegisteredUser = new ArrayList<Role>();
		Role role = this.getRoleByName("ROLE_USER");
		rolesForRegisteredUser.add(role);
		
		return rolesForRegisteredUser;
	}
}
