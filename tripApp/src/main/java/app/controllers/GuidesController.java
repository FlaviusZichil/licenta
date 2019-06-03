package app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.GuideDTO;
import app.dto.TripDTO;
import app.entities.Guide;
import app.entities.Trip;
import app.models.AllGuidesViewModel;
import app.models.TripViewModel;
import app.repositories.GuideRepository;
import app.utils.Conversion;

@Controller
public class GuidesController {

	@Autowired
	private GuideRepository guideRepository;

	@Autowired
	private Conversion conversion;

	@GetMapping("/guides")
	public String getGuides(Model model, AllGuidesViewModel allGuidesViewModel) {
		loadDataOnPage(model, allGuidesViewModel);
		return "views/all/allGuidesView";
	}

	@PostMapping("/guides")
	public String guidesActions(Model model, TripViewModel tripViewModel, AllGuidesViewModel allGuidesViewModel, 
								@RequestParam MultiValueMap<String, String> tripsForGuideAction) {
		
		if(tripsForGuideAction != null) {
			for(Map.Entry<String, List<String>> entry : tripsForGuideAction.entrySet()){
				if(entry.getValue().contains("Vezi ascensiuni disponibile")){
					Guide currentGuide = getGuideByGuideId(Integer.parseInt(entry.getKey()));
					if(getTripsDTOForGuide(currentGuide).size() == 0) {
						loadDataOnPage(model, allGuidesViewModel);
						model.addAttribute("noActiveTripForguide", true);
						return "views/all/allGuidesView";
					}
					else {
						tripViewModel.setTripsDTO(getTripsDTOForGuide(currentGuide));
						model.addAttribute("tripViewModel", tripViewModel);	
						return "views/all/allTrips";
					}				
				}
			}
		}	
		return"views/all/allGuidesView";
	}
	
	private void loadDataOnPage(Model model, AllGuidesViewModel allGuidesViewModel) {
		allGuidesViewModel.setGuidesDTO(getAllGuidesDTO());
		model.addAttribute("allGuidesViewModel", allGuidesViewModel);
	}

	private Guide getGuideByGuideId(Integer guideId) {
		for(Guide guide : guideRepository.findAll()) {
			if(guide.getId() == guideId) {
				return guide;
			}
		}
		return null;
	}
	
	private List<TripDTO> getTripsDTOForGuide(Guide guide){
		List<TripDTO> tripsDTO = new ArrayList<>();
		for(Trip trip : guide.getTrips()) {
			tripsDTO.add(conversion.convertFromTripToTripDTO(trip));
		}
		return tripsDTO;
	}

	private List<GuideDTO> getAllGuidesDTO(){
		List<GuideDTO> allGuidesDTO = new ArrayList<>();
		for(Guide guide : guideRepository.findAll()) {
			GuideDTO guideDTO = conversion.convertFromGuideToGuideDTO(guide);
			Integer finishedTrips = getFinishedTripsForGuide(guide);
			guideDTO.setFinishedTrips(finishedTrips);	
			allGuidesDTO.add(guideDTO);
		}
		return allGuidesDTO;
	}

	private Integer getFinishedTripsForGuide(Guide guide) {
		Integer finishedTrips = 0;
		for (Trip trip : guide.getTrips()) {
			if (trip.getStatus().equals("Finished")) {
				finishedTrips++;
			}
		}
		return finishedTrips;
	}
}
