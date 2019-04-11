package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPointsController {

	@GetMapping("/my-points")
	public String getMyPoints() {
		return "views/all/myPoints";
	}
}
