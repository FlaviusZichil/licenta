package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.UserDTO;
import app.entities.City;
import app.entities.UserEntity;
import app.models.PersonalDataViewModel;
import app.repositories.CityRepository;
import app.repositories.UserRepository;
import app.utils.UserUtils;
import app.validators.RegisterValidator;

@Controller
public class PersonalDataController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@GetMapping("/personal-data")
	public String getPersonalData(Model model, PersonalDataViewModel personalDataViewModel, Principal principal) {
		loadPersonalDataPage(principal, model, personalDataViewModel);
		return "views/all/personalDataView";
	}

	@PostMapping("/personal-data")
	public String personalDataActions(Model model, Principal principal, PersonalDataViewModel personalDataViewModel,
									  @RequestParam(name = "submit", required = false) String actionType,
									  @RequestParam(name = "currentPassword", required = false) String currentPassword,
									  @RequestParam(name = "newPassword", required = false) String newPassword,
									  @RequestParam(name = "personalDataInput", required = false) String personalDataInput) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
		switch(actionType) {
			case "Schimba parola":{
				changePasswordForUser(user, currentPassword, newPassword, model);
				loadPersonalDataPage(principal, model, personalDataViewModel);
				break;
			}
			case "Salveaza":{
				savePersonalData(personalDataInput, user, model);
				loadPersonalDataPage(principal, model, personalDataViewModel);
				break;
			}
			case "Anuleaza":{
				loadPersonalDataPage(principal, model, personalDataViewModel);
				break;
			}
		}
		return "views/all/personalDataView";
	}
	
	private void loadPersonalDataPage(Principal principal, Model model, PersonalDataViewModel personalDataViewModel) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		UserDTO userDTO = userUtils.convertFromUserToUserDTO(user);
		if(user.getRole().getName().equals("ROLE_GUIDE")) {
			userDTO.setDescription(user.getGuide().getDescription());
		}
		personalDataViewModel.setUserDTO(userDTO);
		model.addAttribute("personalDataViewModel", personalDataViewModel);
		model.addAttribute("allCities", getAllCitiesNames());
	}
	
	private List<String> getAllCitiesNames() {
		List<String> cityNames = new ArrayList<>();
		for(City city : cityRepository.findAll()) {
			cityNames.add(city.getName());
		}
		Collections.sort(cityNames);
		return cityNames;
	}

	private void changePasswordForUser(UserEntity user, String currentPassword, String newPassword, Model model) {
		if(isCurrentPasswordCorect(currentPassword, user) && RegisterValidator.isPasswordValid(newPassword)) {
			user.setPassword(newPassword);
			userRepository.save(user);
			model.addAttribute("passwordChangedSuccessfully", true);
			return;
		}
		if(!isCurrentPasswordCorect(currentPassword, user)) {
			model.addAttribute("wrongCurrentPassword", true);
		}
		if(!RegisterValidator.isPasswordValid(newPassword)) {
			model.addAttribute("wrongNewPassword", true);
		}
	}
	
	private boolean isCurrentPasswordCorect(String currentPassword, UserEntity user) {
		if(!user.getPassword().equals(currentPassword)) {
			return false;
		}
		return true;
	}
	
	private void savePersonalData(String personalDataInput, UserEntity user, Model model) {
		List<String> personalDates = new ArrayList<>(Arrays.asList(personalDataInput.split(",")));
		verifyPersonalData(personalDates, user, model);
		user.setCity(getCityByName(personalDates.get(4)));
		userRepository.save(user);
	}
	
	private City getCityByName(String cityName) {
		for(City city : cityRepository.findAll()) {
			if(city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}
	
	private void verifyPersonalData(List<String> personalDates, UserEntity user, Model model) {		
		verifyFirstName(model, personalDates, user);
		verifyLastName(model, personalDates, user);
		verifyEmail(model, personalDates, user);
		verifyBirthDate(model, personalDates, user);
		verifyExperience(model, personalDates, user);
		verifyDescription(model, personalDates, user);
	}
	
	private void verifyDescription(Model model, List<String> personalDates, UserEntity user) {
		System.out.println(personalDates.toString());
		if(personalDates.size() > 6 && personalDates.get(6).length() > 10) {
			user.getGuide().setDescription(personalDates.get(6));
		}
		else {
			model.addAttribute("wrongDescription", true);
		}
	}
	
	private void verifyLastName(Model model, List<String> personalDates, UserEntity user) {
		if(RegisterValidator.isNameValid(personalDates.get(0))) {
			user.setLastName(RegisterValidator.formatNameProperly(personalDates.get(0)));
		}
		else {
			model.addAttribute("wrongLastName", true);
		}
	}
	
	private void verifyFirstName(Model model, List<String> personalDates, UserEntity user) {
		if(RegisterValidator.isNameValid(personalDates.get(1))) {
			user.setFirstName(RegisterValidator.formatNameProperly(personalDates.get(1)));
		}
		else {
			model.addAttribute("wrongFirstName", true);
		}
	}
	
	private void verifyEmail(Model model, List<String> personalDates, UserEntity user) {
		if(RegisterValidator.isEmailValid(personalDates.get(2))) {
			user.setEmail(personalDates.get(2));
		}
		else {
			model.addAttribute("wrongEmail", true);
		}
	}
	
	private void verifyBirthDate(Model model, List<String> personalDates, UserEntity user) {
		if(RegisterValidator.isDateValid(personalDates.get(3))) {
			user.setBirthDate(personalDates.get(3));
		}
		else {
			model.addAttribute("wrongBirthDate", true);
		}
	}
	
	private void verifyExperience(Model model, List<String> personalDates, UserEntity user) {
		if(personalDates.size() > 5) {
			user.setExperience(personalDates.get(5));
		}
	}
}
