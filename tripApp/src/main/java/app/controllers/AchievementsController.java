package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.MedalDTO;
import app.dto.TripDTO;
import app.entities.Medal;
import app.entities.Trip;
import app.entities.UserEntity;
import app.entities.UserMedal;
import app.models.AllMedalsViewModel;
import app.models.TripViewModel;
import app.repositories.MedalRepository;
import app.repositories.TripRepository;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class AchievementsController {
	
	@Autowired
	private MedalRepository medalRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserUtils userUtils;
	
	@GetMapping("/achievements")
	public String getAchievements(Model model, AllMedalsViewModel allMedalViewModel, Principal principal, HttpSession session) {			
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		allMedalViewModel.setMedalsDTO(this.getAllMedalsForUser(user));
		model.addAttribute("allMedalViewModel", allMedalViewModel);
		
		Integer numberOfMedals = this.getNumberOfMedalsForUser(user);
		model.addAttribute("medalsNumber", numberOfMedals);
		
		Integer highestAltitude = this.getHighestAltitudeForUser(user);
		model.addAttribute("highestAltitude", highestAltitude);
		
		Integer numberOfFinishedTrips = this.getNumberOFAllFinishedTripsForUser(user);
		model.addAttribute("numberOfFinishedTrips", numberOfFinishedTrips);
		
		if(session.getAttribute("noAvailableTripsForThatMedal") != null) {
			if((boolean)session.getAttribute("noAvailableTripsForThatMedal")) {
				model.addAttribute("noAvailableTripsForThatMedal", true);
				session.setAttribute("noAvailableTripsForThatMedal", false);
			}
		}	
		return "views/all/achievementsView";
	}
	
	@PostMapping("/achievements")
	public String achievementsActions(Model model, TripViewModel tripViewModel, HttpSession session, Principal principal,
									  @RequestParam MultiValueMap<String, String> selectedMedal) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());

		if(selectedMedal != null) {
			for(Map.Entry<String, List<String>> entry : selectedMedal.entrySet()){
				if(entry.getValue().contains("Obtine medalia")){
					List<TripDTO> tripsDTOForUser = new ArrayList<>();
					for(Trip trip : tripRepository.findAll()) {
						if(trip.getPeak().getId() == Integer.parseInt(entry.getKey()) && !this.isUserRegisteredForTrip(user, trip)) {
							tripsDTOForUser.add(TripUtils.convertFromTripToTripDTO(trip));
						}
					}
					if(tripsDTOForUser.size() == 0) {
						session.setAttribute("noAvailableTripsForThatMedal", true);
						return "redirect:/achievements";
					}
					tripViewModel.setTripsDTO(tripsDTOForUser);
					model.addAttribute("tripViewModel", tripViewModel);	
					return "views/all/allTrips";
				}
			}
		}
		System.out.println("a intrat aici3");
		return "views/all/achievementsView";
	}
	
	private boolean isUserRegisteredForTrip(UserEntity user, Trip trip) {
		for (Trip tripObj : user.getTrips()) {
			if (tripObj.getId() == trip.getId()) {
				return true;
			}
		}
		return false;
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
