package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		personalDataViewModel.setUserDTO(userUtils.convertFromUserToUserDTO(user));
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
}
