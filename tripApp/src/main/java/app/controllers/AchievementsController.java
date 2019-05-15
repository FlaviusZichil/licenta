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
import app.entities.Trip;
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
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		allMedalViewModel.setMedalsDTO(this.getAllMedalsForUser(user));
		model.addAttribute("allMedalViewModel", allMedalViewModel);
		
		Integer numberOfMedals = this.getNumberOfMedalsForUser(user);
		model.addAttribute("medalsNumber", numberOfMedals);
		
		Integer highestAltitude = this.getHighestAltitudeForUser(user);
		model.addAttribute("highestAltitude", highestAltitude);
		
		Integer numberOfFinishedTrips = this.getNumberOFAllFinishedTripsForUser(user);
		model.addAttribute("numberOfFinishedTrips", numberOfFinishedTrips);
		
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
	
	private Integer getNumberOfMedalsForUser(UserEntity user) {
		Integer numberOfMedals = 0;
		for(MedalDTO medalDTO : this.getAllMedalsForUser(user)) {
			if(medalDTO.isOwned()) {
				numberOfMedals++;
			}
		}
		return numberOfMedals;
	}
	
	private Integer getHighestAltitudeForUser(UserEntity user) {
		Integer highestAltitude = 0;
		for(MedalDTO medalDTO : this.getAllMedalsForUser(user)) {
			if(medalDTO.getPeakDTO().getAltitude() > highestAltitude && medalDTO.isOwned()) {
				highestAltitude = medalDTO.getPeakDTO().getAltitude();
			}
		}
		return highestAltitude;
	}
	
	private Integer getNumberOFAllFinishedTripsForUser(UserEntity user) {
		Integer numberOfFinishedTrips = 0;
		for(Trip trip : user.getTrips()) {
			if(trip.getStatus().equals("Finished")) {
				numberOfFinishedTrips++;
			}
		}
		return numberOfFinishedTrips;
	}
}
