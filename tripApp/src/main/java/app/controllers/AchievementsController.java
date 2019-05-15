package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dto.MedalDTO;
import app.entities.Medal;
import app.models.AllMedalsViewModel;
import app.repositories.MedalRepository;
import app.utils.TripUtils;

@Controller
public class AchievementsController {
	
	@Autowired
	private MedalRepository medalRepository;
	
	@GetMapping("/achievements")
	public String getAchievements(Model model, AllMedalsViewModel allMedalViewModel) {
		List<MedalDTO> medalsDTO = this.getAllMedalsDTO();
		allMedalViewModel.setMedalsDTO(medalsDTO);
		model.addAttribute("allMedalViewModel", allMedalViewModel);
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
}
