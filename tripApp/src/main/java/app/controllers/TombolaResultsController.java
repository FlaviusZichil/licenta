package app.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.TombolaResultsDTO;
import app.dto.UserDTO;
import app.entities.Tombola;
import app.entities.UserEntity;
import app.models.TombolaResultsViewModel;
import app.repositories.TombolaRepository;
import app.utils.UserUtils;

@Controller
public class TombolaResultsController {
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private TombolaRepository tombolaRepository;

	@GetMapping("/tombola-results")
	public String getTombolaResultsPage(Model model) {
		setParticipantsOnModel(model);
		if(getAllTombolaParticipants().size() == 0) {
			model.addAttribute("wereWinnersPicked", true);
		}
		return "views/admin/tombolaResultsView";
	}
	
	@PostMapping("/tombola-results")
	public String tombolaResultsActions(Model model, HttpSession session,
										@RequestParam(name = "submit", required = false) String getResults) {
		if(getResults.equals("Extrage castigatorii") && !wereWinnersPicked()) {
			setWinnersOnModel(model);
			setParticipantsOnModel(model);
		}
		else {
			setParticipantsOnModel(model);
			return "redirect:/tombola-results";
		}
		return "views/admin/tombolaResultsView";
	}
	
	private void setWinnersOnModel(Model model) {
		List<TombolaResultsDTO> results = getTombolaResults(getAllTombolaParticipants());
		model.addAttribute("results", results);
	}
	
	private boolean wereWinnersPicked() {
		Integer winners = 0;
		for(Tombola tombola : tombolaRepository.findAll()) {
			if(LocalDate.parse(tombola.getDate()).getMonthValue() == LocalDate.now().getMonthValue() - 1) {
				if(tombola.getStatus().equals("Locul 1") || tombola.getStatus().equals("Locul 2") || tombola.getStatus().equals("Locul 3")) {
					winners++;
				}
			}
		}
		if(winners == 3) {
			return true;
		}
		return false;
	}
	
	private void setParticipantsOnModel(Model model) {
		TombolaResultsViewModel tombolaResultsViewModel = new TombolaResultsViewModel();
		tombolaResultsViewModel.setTombolaResultsDTO(getAllTombolaParticipants());
		model.addAttribute("tombolaResultsViewModel", tombolaResultsViewModel);
	}
	
	private TombolaResultsDTO convertFromTombolaToTombolaResultDTO(Tombola tombola) {
		UserDTO userDTO = userUtils.convertFromUserToUserDTO(tombola.getUser());
		TombolaResultsDTO tombolaResultsDTO = new TombolaResultsDTO(userDTO, tombola.getDate(), tombola.getStatus());
		return tombolaResultsDTO;
	}
	
	private List<TombolaResultsDTO> getTombolaResults(List<TombolaResultsDTO> results) {
		Collections.shuffle(results);
		if(results.size() < 3) {
			return null;
		}
		if(results.size() >= 3) {
			results.get(0).setStatus("Locul 1");
			results.get(1).setStatus("Locul 2");
			results.get(2).setStatus("Locul 3");
		}
		if(results.size() >= 8) {
			for(int i = 3; i <= results.size(); i++) {
				if(i <= 8) {
					results.get(i).setStatus("Rezerva");
				}
				else {
					results.get(i).setStatus("Necastigator");
				}
			}
		}
		if(results.size() > 3 && results.size() < 8) {
			for(int i = 3; i < results.size(); i++) {
				results.get(i).setStatus("Rezerva");
			}
		}
		saveResults(results);
		return results;
	}
	
	private void saveResults(List<TombolaResultsDTO> results) {
		for(TombolaResultsDTO result : results) {
			Tombola tombola = getRegistrationByUserAndDate(result.getUserDTO(), result.getDate());
			tombola.setStatus(result.getStatus());		
			tombolaRepository.save(tombola);
		}
	}
	
	private Tombola getRegistrationByUserAndDate(UserDTO userDTO, String date) {
		for(Tombola tombola : tombolaRepository.findAll()) {
			if(tombola.getDate().equals(date) && tombola.getUser().getId() == userDTO.getId()) {
				return tombola;
			}
		}
		return null;
	}
	
	private List<TombolaResultsDTO> getAllTombolaParticipants(){
		List<TombolaResultsDTO> tombolaParticipants = new ArrayList<>();
		for(Tombola tombola : tombolaRepository.findAll()) {
			Integer registerMonth = LocalDate.parse(tombola.getDate()).getMonthValue();
			Integer registerYear = LocalDate.parse(tombola.getDate()).getYear();
			Integer currentMonth = LocalDate.now().getMonthValue();
			Integer currentYear = LocalDate.now().getYear();			
			// cu int dadea false nu stiu de ce
			if(registerMonth.toString().equals(String.valueOf(currentMonth - 1)) && registerYear.toString().equals(currentYear.toString()) && tombola.getStatus().equals("new")) {
				tombolaParticipants.add(convertFromTombolaToTombolaResultDTO(tombola));
			}			
			if(registerMonth.toString().equals("12") && currentMonth.toString().equals("1") && registerYear.toString().equals(String.valueOf(currentYear - 1)) && tombola.getStatus().equals("new")) {
				tombolaParticipants.add(convertFromTombolaToTombolaResultDTO(tombola));
			}
		}
		return tombolaParticipants;
	}
}
