package app.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.PeakDTO;
import app.dto.PointDTO;
import app.dto.TripDTO;
import app.entities.Guide;
import app.entities.Peak;
import app.entities.Point;
import app.entities.Route;
import app.entities.RoutePoint;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.AddTripViewModel;
import app.repositories.GuideRepository;
import app.repositories.PeakRepository;
import app.repositories.PointRepository;
import app.repositories.RoutePointRepository;
import app.repositories.RouteRepository;
import app.repositories.TripRepository;
import app.repositories.UserRepository;
import app.utils.Conversion;
import app.utils.TripUtils;
import app.utils.UserUtils;
import app.validators.RegisterValidator;

@Controller
public class AddTripController {
	@Autowired
	private PeakRepository peakRepository;	
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
		setDataOnModel(addTripViewModel, model, null);
		
		if(principal != null) {
			UserEntity user = userUtils.getUserByEmail(principal.getName());
			if(user.getRole().getName().equals("ROLE_GUIDE") && TripUtils.getNumberOfFinishedTripsWithActiveStatusForGuide(user.getGuide()) > 0) {
				model.addAttribute("guideHasUnfinishedTrips", true);
			}
		}
		
		return "views/guide/guideAddTrip";
	}
	
	@PostMapping("/add-trip")
	public String postAddTrip(Model model, Principal principal, AddTripViewModel addTripViewModel, TripDTO tripDTO,
							  @RequestParam(name = "addTrip", required = false) String addTrip,
							  @RequestParam(name = "suggestTrip", required = false) String suggestTrip,
						      @RequestParam(name = "peak", required = false) String peak,
							  @RequestParam(name = "startDate", required = false) String startDate,
							  @RequestParam(name = "endDate", required = false) String endDate,
							  @RequestParam(name = "initialPoint", required = false) String initialPoint,
							  @RequestParam(name = "finalPoint", required = false) String finalPoint,
							  @RequestParam(name = "intermediatePoint", required = false) String intermediatePoint,
							  @RequestParam(name = "difficulty", required = false) String difficulty,
							  @RequestParam(name = "capacity", required = false) String capacity,
							  @RequestParam(name = "points", required = false) String points) throws ParseException {
		
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		boolean areDatesValid = true;
		boolean isCapacityValid = true;
		boolean arePointsValid = true;
		boolean isLocationValid = true;
		boolean areRoutePointsValid = true;
		
		if(!areDatesValid(startDate, endDate)) {
			model.addAttribute("invalidDates", true);
			areDatesValid = false;
		}
		
		if(Integer.parseInt(capacity) < 5 && Integer.parseInt(capacity) > 50) {
			model.addAttribute("invalidCapacity", true);
			isCapacityValid = false;
		}
		else {
			tripDTO.setCapacity(Integer.parseInt(capacity));
		}
		
		if(Integer.parseInt(points) < 50 && Integer.parseInt(points) > 75) {
			model.addAttribute("invalidPoints", true);
			arePointsValid = false;
		}
		else {
			tripDTO.setPoints(Integer.parseInt(points));
		}
		
		if(getPeakByName(peak) == null) {
			model.addAttribute("invalidLocation", true);
			isLocationValid = false;
		}
		else {
			tripDTO.setPeakDTO(conversion.convertFromPeakToPeakDTO(getPeakByName(peak)));
		}

		if(!arePointsValid(createListOfPoints(initialPoint, intermediatePoint, finalPoint), peak)) {
			model.addAttribute("invalidRoutePoints", true);
			areRoutePointsValid = false;
		}
		
		if(areDatesValid && isCapacityValid && arePointsValid && isLocationValid && areRoutePointsValid) {
			if(addTrip != null) {
				if(!hasTripsInPeriod(startDate, endDate, user.getGuide())) {
					addTripToDatabase(peak, startDate, endDate, initialPoint, intermediatePoint, finalPoint, Integer.parseInt(capacity), difficulty, Integer.parseInt(points), "Active", principal);
					model.addAttribute("tripAddedWithSuccess", true);
				}
				else {
					model.addAttribute("hasTripsInThatPeriod", true);
				}
			}
			if(suggestTrip != null) {
				addTripToDatabase(peak, startDate, endDate, initialPoint, intermediatePoint, finalPoint, Integer.parseInt(capacity), difficulty, Integer.parseInt(points), "Suggested", principal);
				model.addAttribute("tripAddedWithSuccess", true);
			}
			
		}
		setDataOnModel(addTripViewModel, model, tripDTO);
		return "views/guide/guideAddTrip";
	}
	
	private void setDataOnModel(AddTripViewModel addTripViewModel, Model model, TripDTO tripDTO) {
		addTripViewModel.setPeaksDTO(getPeaksDTO());
		addTripViewModel.setPointsDTO(getPointsDTO());
		model.addAttribute("pointsDTO", getPointsDTO());
		model.addAttribute("addTripViewModel", addTripViewModel);
		model.addAttribute("tripDTO", tripDTO);
	}
	
	private List<PointDTO> createListOfPoints(String initialPoint, String intermediarePoints, String finalPoint){
		List<PointDTO> pointsDTO = new ArrayList<>();
		pointsDTO.add(conversion.convertFromPointToPointDTO(getPointByName(initialPoint.substring(0, initialPoint.indexOf(",")).trim())));
		pointsDTO.add(conversion.convertFromPointToPointDTO(getPointByName(finalPoint.substring(0, finalPoint.indexOf(",")).trim())));
		List<String> intermediatePoints = new ArrayList<>(Arrays.asList(intermediarePoints.split(",")));
		for(String point : intermediatePoints) {
			pointsDTO.add(conversion.convertFromPointToPointDTO(getPointByName(point)));
		}
		return pointsDTO;
	}
	
	private void addTripToDatabase(String peakName, String startDate, 
								   String endDate, String initialPoint, String interemediatePoints, String finalPoint, 
								   Integer capacity, String difficulty, Integer points, String status, Principal principal) {		
		Peak peak = getPeakByName(peakName);
		
		Route route = addRoute(difficulty);
		addRoutePoints(route, interemediatePoints, initialPoint, finalPoint);
		
		Guide guide = this.getGuideByUserId(principal);
		
		Trip trip = null;
		if(status.equals("Active")) {
			trip = new Trip(capacity, startDate, endDate, status, points, guide, route, peak);
		}
		if(status.equals("Suggested")) {
			trip = new Trip(capacity, startDate, endDate, status, points, null, route, peak);
		}
		tripRepository.save(trip);		
	}
	
	public <T> boolean hasDuplicates(Iterable<T> all) {
	    Set<T> set = new HashSet<T>();
	    for (T each: all) if (!set.add(each)) return true;
	    return false;
	}
	
	private boolean hasTripsInPeriod(String startDate, String endDate, Guide guide) {
		for(Trip trip : guide.getTrips()) {
			LocalDate tripStartDate = LocalDate.parse(trip.getStartDate());
			LocalDate tripEndDate = LocalDate.parse(trip.getEndDate());
			LocalDate givenStartDate = LocalDate.parse(startDate);
			LocalDate givenEndDate = LocalDate.parse(endDate);
			if(tripStartDate.isEqual(givenStartDate) || 
					tripStartDate.isEqual(givenEndDate) || 
					tripEndDate.isEqual(givenStartDate) || 
					tripEndDate.isEqual(givenEndDate) || 
					(
					givenStartDate.isAfter(tripStartDate) &&
					givenStartDate.isBefore(tripEndDate)
					) ||
					(
					givenEndDate.isAfter(tripStartDate) &&
					givenEndDate.isBefore(tripEndDate)
					)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean arePointsValid(List<PointDTO> pointsDTO, String peak) {
		Peak peakForPoints = getPeakByName(peak);

		if(hasDuplicates(pointsDTO)) {
			return false;
		}
		
		for(PointDTO pointDTO : pointsDTO) {
			if(!pointDTO.getMountainDTO().getName().equals(peakForPoints.getMountain().getMountainName())) {
				return false;
			}
		}
		return true;
	}
	
	private Route addRoute(String difficulty) {
		Route route = new Route();
		route.setDifficulty(difficulty);	
		routeRepository.save(route);
		return route;
	}
	
	private void addRoutePoints(Route route, String routePoints, String initialPoint, String finalPoint) {
		Integer order = 1;
		Point initialPointForTrip = getPointByName(initialPoint.substring(0, initialPoint.indexOf(",")).trim());
		routePointRepository.save(new RoutePoint(route, initialPointForTrip, order.toString()));
		
		List<String> interemediatePointsForTrip = new ArrayList<>(Arrays.asList(routePoints.split(",")));
		
		for(String intermediatePoint : interemediatePointsForTrip) {
			Point intermediatePointForTrip = this.getPointByName(intermediatePoint);
			order++;
			routePointRepository.save(new RoutePoint(route, intermediatePointForTrip, order.toString()));
		}
				
		Point finalPointForTrip = this.getPointByName(finalPoint.substring(0, finalPoint.indexOf(",")).trim());
		order++;
		routePointRepository.save(new RoutePoint(route, finalPointForTrip, order.toString()));
	}
	
	private boolean areDatesValid(String startDate, String endDate) throws ParseException{
		if(RegisterValidator.getYearFromDate(startDate).length() != 4 || RegisterValidator.getYearFromDate(endDate).length() != 4) {
			return false;
		}
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
	
	private List<PointDTO> getPointsDTO(){
		List<PointDTO> pointsDTO = new ArrayList<>();	
		for(Point point : pointRepository.findAll()) {
			pointsDTO.add(new PointDTO(point.getId(), point.getPointName(), conversion.convertFromMountainToMountainDTO(point.getMountain())));
		}		
		return TripUtils.sortPointsByMountain(pointsDTO);
	}
	
	private UserEntity getUserByEmail(String email) {		
		for(UserEntity user : userRepository.findAll()) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
	
	private Guide getGuideByUserId(Principal principal) {	
		for(Guide guide : guideRepository.findAll()) {
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
	
	private Peak getPeakByName(String peakName) {		
		for(Peak peak : peakRepository.findAll()) {					
			if(peak.getPeakName().trim().toUpperCase().equals(peakName.trim().toUpperCase())) {
				return peak;
			}
		}
		return null;
	}
}
