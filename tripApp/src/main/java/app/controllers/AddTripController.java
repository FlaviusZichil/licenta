package app.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		model.addAttribute("pointsDTO", this.getPointsDTO());
		model.addAttribute("addTripViewModel", addTripViewModel);
		
		return "views/guide/guideAddTrip";
	}
	
	@PostMapping("/add-trip")
	public String postAddTrip(@RequestParam(name = "startDate", required = false) String startDate,
							  @RequestParam(name = "endDate", required = false) String endDate) {
		
		System.out.println(startDate);
		System.out.println(endDate);
		
		return "redirect:/";
	}
	
	private boolean verifyDates(String startDate, String endDate) throws ParseException {
		Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
		Date secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long daysDifference = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		
		if(firstDate.after(secondDate) || firstDate.before(Timestamp.valueOf(LocalDateTime.now())) || daysDifference > 4) {
			return false;
		}
		return true;
	}
	
	private List<PeakDTO> getPeaksDTO(){
		Iterable<Peak> allPeaks = peakRepository.findAll();
		List<Peak> peaks = TripUtils.convertFromIterableToList(allPeaks);
		List<PeakDTO> peaksDTO = new ArrayList<>();
		
		for(Peak peak : peaks) {
			PeakDTO peakDTO = TripUtils.convertFromPeakToPeakDTO(peak);
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
}
