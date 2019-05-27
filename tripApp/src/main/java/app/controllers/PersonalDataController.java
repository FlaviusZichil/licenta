package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonalDataController {

	@GetMapping("/personal-data")
	public String getPersonalData(Model model) {
		return "views/all/personalDataView";
	}
	
	@PostMapping("/personal-data")
	public String personalDataActions(Model model) {
		return "views/all/personalDataView";
	}
}
