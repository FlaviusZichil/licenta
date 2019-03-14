package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/adminHome")
	public String getAdminHome() {
		return "views/admin/adminHome";
	}
}
