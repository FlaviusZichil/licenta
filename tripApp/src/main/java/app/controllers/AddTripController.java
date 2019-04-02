package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddTripController {

	@GetMapping("/add-trip")
	public String addTrip() {
		System.out.println("salutare test");
		return "views/guide/guideAddTrip";
	}
}
