package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dto.MedalDTO;
import app.entities.Medal;
import app.entities.UserEntity;
import app.entities.UserMedal;
import app.models.AllMedalsViewModel;
import app.repositories.MedalRepository;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class AchievementsController {
	
	@Autowired
	private MedalRepository medalRepository;
	
	@Autowired
	private UserUtils userUtils;
	
	@GetMapping("/achievements")
	public String getAchievements(Model model, AllMedalsViewModel allMedalViewModel, Principal principal) {
//		List<MedalDTO> medalsDTO = this.getAllMedalsDTO();
			
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		allMedalViewModel.setMedalsDTO(this.getAllMedalsForUser(user));
		model.addAttribute("allMedalViewModel", allMedalViewModel);
		
		for(MedalDTO medal : this.getAllMedalsForUser(user)) {
			System.out.println(medal.toString());
		}
		
		return "views/all/achievementsView";
	}
	
	@PostMapping("/achievements")
	public String achievementsActions() {
		return "views/all/achievementsView";
	}
	
	private List<MedalDTO> getAllMedalsDTO(){
		List<MedalDTO> medalsDTO = new ArrayList<>();
		for(Medal medal : medalRepository.findAll()) {
			medalsDTO.add(this.convertFromMedalToMedalDTO(medal));
		}
		return medalsDTO;
	}

	private MedalDTO convertFromMedalToMedalDTO(Medal medal) {
		MedalDTO medalDTO = new MedalDTO(medal.getMedal_id(), TripUtils.convertFromPeakToPeakDTO(medal.getPeak()));
		return medalDTO;
	}
	
	private List<MedalDTO> getAllMedalsForUser(UserEntity user){
		List<MedalDTO> medalsForUser = this.getAllMedalsDTO();
		
		for(MedalDTO medalDTO : medalsForUser) {
			for(UserMedal userMedal : user.getMedals()) {
				if(userMedal.getMedal().getMedal_id() == medalDTO.getMedal_id()) {
					medalDTO.setOwned(true);
				}
			}
		}
		return medalsForUser;
	}
}
