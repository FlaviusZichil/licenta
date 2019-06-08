package app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.documents.Article;
import app.dto.UsersReportsDTO;
import app.entities.Role;
import app.entities.UserEntity;
import app.entities.UserTrip;
import app.models.UsersReportsViewModel;
import app.repositories.ArticleRepository;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import app.repositories.UserTripRepository;
import app.utils.UserUtils;

@Controller
public class UsersReportController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserTripRepository userTripRepository;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private UserUtils userUtils;

	@GetMapping("/users-reports")
	public String getUsersReports(Model model, UsersReportsViewModel usersReportsViewModel) {
		getReports(usersReportsViewModel);
		model.addAttribute("usersReportsViewModel", usersReportsViewModel);
		return "views/admin/usersReportsView";
	}
	
	@PostMapping("/users-reports")
	public String usersReportsActions(Model model,
									  @RequestParam MultiValueMap<String, String> action) {
		
		if(action != null) {
			for(Map.Entry<String, List<String>> selectedAction : action.entrySet()){
				if(selectedAction.getValue().contains("Blocheaza")){
					blockUserAccount(Integer.parseInt(selectedAction.getKey()));
					return "redirect:/users-reports";
				}
			}
			
			for(Map.Entry<String, List<String>> selectedAction : action.entrySet()){
				if(selectedAction.getValue().contains("Detalii")){
					String redirectUrl = "user-details?id=" + selectedAction.getKey();
					return "redirect:/" + redirectUrl;
				}
			}
		}
		return "views/admin/usersReportsView";
	}
	
	private void blockUserAccount(Integer userId) {
		UserEntity user = userUtils.getUserById(userId);
		user.setBlocked(true);
		user.setPassword("accountBlocked_" + generateRandomId());

		userRepository.save(user);
	}
	
	public String generateRandomId() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 10) {
            int index = (int) (random.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
	}
	
	private void getReports(UsersReportsViewModel usersReportsViewModel){
		List<UsersReportsDTO> usersReports = new ArrayList<>();
		List<UsersReportsDTO> stafMembersReports = new ArrayList<>();
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER")) {
				usersReports.add(new UsersReportsDTO(userUtils.convertFromUserToUserDTO(user), userUtils.getFinishedTripsForUser(user), userUtils.getAbsencesForUser(user), 0));
			}
			if(user.getRole().getName().equals("ROLE_STAFF")) {
				stafMembersReports.add(new UsersReportsDTO(userUtils.convertFromUserToUserDTO(user), userUtils.getFinishedTripsForUser(user), userUtils.getAbsencesForUser(user), userUtils.getArticleForUser(user)));
			}
		}
		usersReportsViewModel.setStaffMembers(stafMembersReports);
		usersReportsViewModel.setUsers(usersReports);
	}
}
