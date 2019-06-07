package app.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dto.TombolaResultsDTO;
import app.dto.UserDTO;
import app.entities.Tombola;
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
	public String getTombolaResultsPage(Model model, TombolaResultsViewModel tombolaResultsViewModel) {
		tombolaResultsViewModel.setTombolaResultsDTO(getAllTombolaParticipants());
		model.addAttribute("tombolaResultsViewModel", tombolaResultsViewModel);
		return "views/admin/tombolaResultsView";
	}
	
	@PostMapping("/tombola-results")
	public String tombolaResultsActions(Model model) {
		return "views/admin/tombolaResultsView";
	}
	
	private TombolaResultsDTO convertFromTombolaToTombolaResultDTO(Tombola tombola) {
		UserDTO userDTO = userUtils.convertFromUserToUserDTO(tombola.getUser());
		TombolaResultsDTO tombolaResultsDTO = new TombolaResultsDTO(userDTO, tombola.getDate(), tombola.getStatus());
		return tombolaResultsDTO;
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
