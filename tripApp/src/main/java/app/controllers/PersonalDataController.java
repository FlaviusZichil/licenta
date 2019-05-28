package app.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.entities.UserEntity;
import app.models.PersonalDataViewModel;
import app.utils.Conversion;
import app.utils.UserUtils;

@Controller
public class PersonalDataController {

	@Autowired
	private UserUtils userUtils;
	
	@GetMapping("/personal-data")
	public String getPersonalData(Model model, PersonalDataViewModel personalDataViewModel, Principal principal) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		personalDataViewModel.setUserDTO(userUtils.convertFromUserToUserDTO(user));
		model.addAttribute("personalDataViewModel", personalDataViewModel);
		return "views/all/personalDataView";
	}
	
	@PostMapping("/personal-data")
	public String personalDataActions(Model model) {
		return "views/all/personalDataView";
	}
}
