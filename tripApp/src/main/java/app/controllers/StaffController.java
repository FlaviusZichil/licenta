package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffController {

	@GetMapping("/staffHome")
	public String getStaffHome() {
		return "views/staff/staffHome";
	}
}
