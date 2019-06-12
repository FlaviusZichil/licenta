package app.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import app.entities.Guide;
import app.entities.Mountain;
import app.entities.Peak;
import app.entities.Point;
import app.entities.Route;
import app.entities.RoutePoint;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.AddTripViewModel;
import app.repositories.CityRepository;
import app.repositories.GuideRepository;
import app.repositories.MountainRepository;
import app.repositories.PeakRepository;
import app.repositories.PointRepository;
import app.repositories.RoutePointRepository;
import app.repositories.RouteRepository;
import app.repositories.TripRepository;
import app.repositories.UserRepository;
import app.utils.Conversion;
import app.utils.TripUtils;
import app.utils.UserUtils;

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
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GuideRepository guideRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private RoutePointRepository routePointRepository;
	
	@Autowired
	private Conversion conversion;
	
	@Autowired
	private UserUtils userUtils;
	
	@GetMapping("/add-trip")
	public String addTrip(Model model, AddTripViewModel addTripViewModel, Principal principal) {
		
		addTripViewModel.setPeaksDTO(this.getPeaksDTO());
		addTripViewModel.setMountainsDTO(this.getMountainsDTO());
		addTripViewModel.setCitiesDTO(this.getCitiesDTO());
		addTripViewModel.setPointsDTO(this.getPointsDTO());
		model.addAttribute("pointsDTO", this.getPointsDTO());
		model.addAttribute("addTripViewModel", addTripViewModel);
		
		if(principal != null) {
			UserEntity user = userUtils.getUserByEmail(principal.getName());

			if(user.getRole().getName().equals("ROLE_GUIDE") && TripUtils.getNumberOfFinishedTripsWithActiveStatusForGuide(user.getGuide()) > 0) {
				model.addAttribute("guideHasUnfinishedTrips", true);
			}
		}
		
		return "views/guide/guideAddTrip";
	}
	
	@PostMapping("/add-trip")
	public String postAddTrip(Model model, Principal principal,
							  @RequestParam(name = "addTrip", required = false) String addTrip,
							  @RequestParam(name = "suggestTrip", required = false) String suggestTrip,
							  @RequestParam(name = "mountain", required = false) String mountain,
							  @RequestParam(name = "city", required = false) String city,
						      @RequestParam(name = "peak", required = false) String peak,
							  @RequestParam(name = "altitude", required = false) String altitude,
							  @RequestParam(name = "startDate", required = false) String startDate,
							  @RequestParam(name = "endDate", required = false) String endDate,
							  @RequestParam(name = "initialPoint", required = false) String initialPoint,
							  @RequestParam(name = "finalPoint", required = false) String finalPoint,
							  @RequestParam(name = "intermediatePoint", required = false) String intermediatePoint,
							  @RequestParam(name = "difficulty", required = false) String difficulty,
							  @RequestParam(name = "capacity", required = false) String capacity,
							  @RequestParam(name = "points", required = false) String points) throws ParseException {
		
		boolean areDatesValid = true;
		boolean isCapacityValid = true;
		boolean arePointsValid = true;
		boolean isLocationValid = true;
		
		if(!areDatesValid(startDate, endDate)) {
			model.addAttribute("invalidDates", true);
			areDatesValid = false;
		}
		
		if(Integer.parseInt(capacity) < 5 && Integer.parseInt(capacity) > 50) {
			model.addAttribute("invalidCapacity", true);
			isCapacityValid = false;
		}
		
		if(Integer.parseInt(points) < 50 && Integer.parseInt(points) > 75) {
			model.addAttribute("invalidPoints", true);
			arePointsValid = false;
		}
		
		if(this.getPeakByName(peak, Integer.parseInt(altitude), city, mountain) == null) {
			model.addAttribute("invalidLocation", true);
			isLocationValid = false;
		}

		if(areDatesValid && isCapacityValid && arePointsValid && isLocationValid) {

			if(addTrip != null) {
				addTripToDatabase(mountain, city, peak, Integer.parseInt(altitude), startDate, endDate, initialPoint, intermediatePoint, finalPoint, Integer.parseInt(capacity), difficulty, Integer.parseInt(points), "Active", principal);
				System.out.println("a adaugta un ghid");
			}
			if(suggestTrip != null) {
				addTripToDatabase(mountain, city, peak, Integer.parseInt(altitude), startDate, endDate, initialPoint, intermediatePoint, finalPoint, Integer.parseInt(capacity), difficulty, Integer.parseInt(points), "Suggested", principal);
				System.out.println("a adaugta un user");
			}
		}

		return "redirect:/all-trips";
	}
	
	private void addTripToDatabase(String mountain, String city, String peak, Integer altitude, String startDate, 
								   String endDate, String initialPoint, String interemediatePoints, String finalPoint, 
								   Integer capacity, String difficulty, Integer points, String status, Principal principal) {
		
		Peak peakFortrip = this.getPeakByName(peak, altitude, city, mountain);
		
		Route route = this.addRoute(difficulty);
		this.addRoutePoints(route, interemediatePoints, initialPoint, finalPoint);
		
		Guide guide = this.getGuideByUserId(principal);
		
		Trip trip = null;
		if(status.equals("Active")) {
			trip = new Trip(capacity, startDate, endDate, status, points, guide, route, peakFortrip);
		}
		if(status.equals("Suggested")) {
			trip = new Trip(capacity, startDate, endDate, status, points, null, route, peakFortrip);
		}
		tripRepository.save(trip);		
	}
	
	private Route addRoute(String difficulty) {
		Route route = new Route();
		route.setDifficulty(difficulty);
		
		routeRepository.save(route);
		return route;
	}
	
	private void addRoutePoints(Route route, String routePoints, String initialPoint, String finalPoint) {
		Integer order = 1;
		Point initialPointForTrip = getPointByName(initialPoint);
		routePointRepository.save(new RoutePoint(route, initialPointForTrip, order.toString()));
		
		List<String> interemediatePointsForTrip = new ArrayList<>(Arrays.asList(routePoints.split(",")));
		
		for(String intermediatePoint : interemediatePointsForTrip) {
			Point intermediatePointForTrip = this.getPointByName(intermediatePoint);
			order++;
			routePointRepository.save(new RoutePoint(route, intermediatePointForTrip, order.toString()));
		}
				
		Point finalPointForTrip = this.getPointByName(finalPoint);
		order++;
		routePointRepository.save(new RoutePoint(route, finalPointForTrip, order.toString()));
	}
	
	private boolean areDatesValid(String startDate, String endDate) throws ParseException {
		LocalDate firstDate = LocalDate.parse(startDate);
		LocalDate secondDate = LocalDate.parse(endDate);		
		Period period = Period.between(firstDate, secondDate);
	    int days = period.getDays();
	    
	    if(firstDate.isBefore(LocalDate.now()) || secondDate.isBefore(LocalDate.now()) || days > 4 || days < 1) {
	    	return false;
	    }
	    return true;
	}
	
	private List<PeakDTO> getPeaksDTO(){
		List<PeakDTO> peaksDTO = new ArrayList<>();
		
		for(Peak peak : peakRepository.findAll()) {
			PeakDTO peakDTO = conversion.convertFromPeakToPeakDTO(peak);
			peaksDTO.add(peakDTO);
		}		
		return peaksDTO;
	}
	
	private List<MountainDTO> getMountainsDTO(){
		List<MountainDTO> mountainsDTO = new ArrayList<>();
		
		for(Mountain mountain : mountainRepository.findAll()) {
			MountainDTO mountainDTO = new MountainDTO(mountain.getMountainName());
			mountainsDTO.add(mountainDTO);
		}		
		return mountainsDTO;
	}
	
	private List<CityDTO> getCitiesDTO(){
		List<CityDTO> citysDTO = new ArrayList<>();
		
		for(City city : cityRepository.findAll()) {
			CityDTO cityDTO = new CityDTO(city.getName(), city.getLatitude(), city.getLongitude());
			citysDTO.add(cityDTO);
		}		
		return citysDTO;
	}
	
	private List<PointDTO> getPointsDTO(){
		List<PointDTO> pointsDTO = new ArrayList<>();
		
		for(Point point : pointRepository.findAll()) {
			PointDTO pointDTO = new PointDTO(point.getId(), point.getPointName());
			pointsDTO.add(pointDTO);
		}		
		return pointsDTO;
	}
	
	private UserEntity getUserByEmail(String email) {
		Iterable<UserEntity> users = userRepository.findAll();
		
		for(UserEntity user : users) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
	
	private Guide getGuideByUserId(Principal principal) {
		Iterable<Guide> guides = guideRepository.findAll();
		
		for(Guide guide : guides) {
			System.out.println(guide.getUser().getId());
			System.out.println(getUserByEmail(principal.getName()).getId());
			if(guide.getUser().getId() == getUserByEmail(principal.getName()).getId()) {
				return guide;
			}
		}
		return null;
	}
	
	private Point getPointByName(String pointName) {
		Iterable<Point> points = pointRepository.findAll();
		
		for(Point point : points) {
			if(point.getPointName().equals(pointName)) {
				return point;
			}
		}
		return null;
	}
	
	private Peak getPeakByName(String peakName, Integer altitude, String city, String mountain) {
		Iterable<Peak> peaks = peakRepository.findAll();
		
		for(Peak peak : peaks) {					
			if(peak.getPeakName().trim().toUpperCase().equals(peakName.trim().toUpperCase()) && 
			   peak.getAltitude().toString().trim().toUpperCase().equals(altitude.toString().trim().toUpperCase()) && 
			   peak.getCity().getName().trim().toUpperCase().equals(city.trim().toUpperCase()) &&
			   peak.getMountain().getMountainName().trim().toUpperCase().equals(mountain.trim().toUpperCase())) {
				return peak;
			}
		}
		return null;
	}
}
