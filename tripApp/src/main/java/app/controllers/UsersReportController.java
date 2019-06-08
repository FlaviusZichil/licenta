package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.documents.Article;
import app.dto.UsersReportsDTO;
import app.entities.UserEntity;
import app.entities.UserTrip;
import app.models.UsersReportsViewModel;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;
import app.repositories.UserTripRepository;
import app.utils.UserUtils;

@Controller
public class UsersReportController {
	@Autowired
	private UserRepository userRepository;
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
	public String usersReportsActions(Model model) {
		return "views/admin/usersReportsView";
	}
	
	private void getReports(UsersReportsViewModel usersReportsViewModel){
		List<UsersReportsDTO> usersReports = new ArrayList<>();
		List<UsersReportsDTO> stafMembersReports = new ArrayList<>();
		for(UserEntity user : userRepository.findAll()) {
			if(user.getRole().getName().equals("ROLE_USER")) {
				usersReports.add(new UsersReportsDTO(userUtils.convertFromUserToUserDTO(user), getFinishedTripsForUser(user), getAbsencesForUser(user), 0));
			}
			if(user.getRole().getName().equals("ROLE_STAFF")) {
				stafMembersReports.add(new UsersReportsDTO(userUtils.convertFromUserToUserDTO(user), getFinishedTripsForUser(user), getAbsencesForUser(user), getArticleForUser(user)));
			}
		}
		usersReportsViewModel.setStaffMembers(stafMembersReports);
		usersReportsViewModel.setUsers(usersReports);
	}
	
	private Integer getFinishedTripsForUser(UserEntity user) {
		Integer finishedTrips = 0;
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getUser().equals(user) && userTrip.getTrip().getStatus().equals("Finished")) {
				finishedTrips++;
			}
		}
		return finishedTrips;
	}
	
	private Integer getAbsencesForUser(UserEntity user) {
		Integer absences = 0;
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getUser().equals(user) && !userTrip.isParticipated()) {
				absences++;
			}
		}
		return absences;
	}
	
	private Integer getArticleForUser(UserEntity user) {
		Integer articlesForUser = 0;
		for(Article article : articleRepository.findAll()) {
			if(article.getUserId() == user.getId()) {
				articlesForUser++;
			}
		}
		return articlesForUser;
	}
}
