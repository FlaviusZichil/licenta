package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dto.CityDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.PointDTO;
import app.entities.City;
import app.entities.Mountain;
import app.entities.Peak;
import app.entities.Point;
import app.models.AddTripViewModel;
import app.repositories.CityRepository;
import app.repositories.MountainRepository;
import app.repositories.PeakRepository;
import app.repositories.PointRepository;
import app.utils.TripUtils;

@Controller
public class AddTripController {

	@Autowired
	private PeakRepository peakRepository;
	
	@Autowired
	private MountainRepository mountainRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private PointRepository pointRepository;
	
	@GetMapping("/add-trip")
	public String addTrip(Model model, AddTripViewModel addTripViewModel) {
		
		addTripViewModel.setPeaksDTO(this.getPeaksDTO());
		addTripViewModel.setMountainsDTO(this.getMountainsDTO());
		addTripViewModel.setCitiesDTO(this.getCitiesDTO());
		addTripViewModel.setPointsDTO(this.getPointsDTO());
		model.addAttribute("addTripViewModel", addTripViewModel);
		
		return "views/guide/guideAddTrip";
	}
	
	@PostMapping("/add-trip")
	public String postAddTrip() {
		
		return "views/guide/guideAddTrip";
	}
	
	private List<PeakDTO> getPeaksDTO(){
		Iterable<Peak> allPeaks = peakRepository.findAll();
		List<Peak> peaks = TripUtils.convertFromIterableToList(allPeaks);
		List<PeakDTO> peaksDTO = new ArrayList<>();
		
		for(Peak peak : peaks) {
			PeakDTO peakDTO = this.convertFromPeaktoPeakDTO(peak);
			peaksDTO.add(peakDTO);
		}		
		return peaksDTO;
	}
	
	private List<MountainDTO> getMountainsDTO(){
		Iterable<Mountain> allMountains = mountainRepository.findAll();
		List<Mountain> mountains = TripUtils.convertFromIterableToList(allMountains);
		List<MountainDTO> mountainsDTO = new ArrayList<>();
		
		for(Mountain mountain : mountains) {
			MountainDTO mountainDTO = new MountainDTO(mountain.getMountainName());
			mountainsDTO.add(mountainDTO);
		}		
		return mountainsDTO;
	}
	
	private List<CityDTO> getCitiesDTO(){
		Iterable<City> allCities = cityRepository.findAll();
		List<City> cities = TripUtils.convertFromIterableToList(allCities);
		List<CityDTO> citysDTO = new ArrayList<>();
		
		for(City city : cities) {
			CityDTO cityDTO = new CityDTO(city.getName(), city.getLatitude(), city.getLongitude());
			citysDTO.add(cityDTO);
		}		
		return citysDTO;
	}
	
	private List<PointDTO> getPointsDTO(){
		Iterable<Point> allPoints = pointRepository.findAll();
		List<Point> points = TripUtils.convertFromIterableToList(allPoints);
		List<PointDTO> pointsDTO = new ArrayList<>();
		
		for(Point point : points) {
			PointDTO pointDTO = new PointDTO(point.getId(), point.getPointName());
			pointsDTO.add(pointDTO);
		}		
		return pointsDTO;
	}
	
	private PeakDTO convertFromPeaktoPeakDTO(Peak peak) {
		CityDTO cityDTO = new CityDTO(peak.getCity().getName(), peak.getCity().getLatitude(), peak.getCity().getLongitude());
		MountainDTO mountainDTO = new MountainDTO(peak.getMountain().getMountainName());
		PeakDTO peakDTO = new PeakDTO(peak.getId(), peak.getPeakName(), peak.getAltitude(), cityDTO, mountainDTO, peak.getTrips());
		
		return peakDTO;
	}
}
