package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.UsersReportsDTO;
import app.entities.UserEntity;
import app.utils.UserUtils;

@Controller
public class UserDetailsController {
	@Autowired
	private UserUtils userUtils;

	@GetMapping("/user-details")
	public String getUserDetails(Model model,
								@RequestParam(name = "id", required = false) String userId) {
		if(userId != null) {
			UserEntity user = userUtils.getUserById(Integer.parseInt(userId));
			UsersReportsDTO userDetails = null;
			if(user.getRole().getName().equals("ROLE_USER")){
				userDetails = new UsersReportsDTO(userUtils.convertFromUserToUserDTO(user), userUtils.getFinishedTripsForUser(user), userUtils.getAbsencesForUser(user), 0);
			}
			if(user.getRole().getName().equals("ROLE_STAFF")){
				userDetails = new UsersReportsDTO(userUtils.convertFromUserToUserDTO(user), userUtils.getFinishedTripsForUser(user), userUtils.getAbsencesForUser(user),  userUtils.getArticleForUser(user));
			}
			model.addAttribute("userDetails", userDetails);
			return "views/admin/userDetailsView";
		}
		return "redirect/users-reports";
	}
	
	@PostMapping("/user-details")
	public String userDetailsActions(Model model) {
		return "views/admin/userDetailsView";
	}
}
