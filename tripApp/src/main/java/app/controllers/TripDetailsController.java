package app.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import app.dto.TripDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.models.TripDetailsViewModel;
import app.repositories.TripRepository;
import app.repositories.UserRepository;
import app.utils.TripUtils;
import app.utils.UserUtils;

@Controller
public class TripDetailsController {

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserUtils userUtils;

	@SessionScope
	@GetMapping("/trip-details")
	public String getTripDetails(Model model, Principal principal, TripDetailsViewModel tripDetailsViewModel,
			HttpSession session, @RequestParam(value = "trip", required = false) String tripId) {

		tripDetailsViewModel.setTripDTO(this.getTripDTOById(Integer.parseInt(tripId)));
		model.addAttribute("tripDetailsViewModel", tripDetailsViewModel);

		UserEntity user = userUtils.getUserByEmail(principal.getName());
		if (isUserRegisteredForTrip(user, Integer.parseInt(tripId))) {
			model.addAttribute("isAlreadyRegisteredForTrip", true);
		} else {
			model.addAttribute("isAlreadyRegisteredForTrip", false);
		}

		session.setAttribute("tripId", tripId);

		return "views/all/tripDetails";
	}

	@PostMapping("/trip-details")
	public String postTripDetails(HttpSession session, Principal principal, Model model,
									@RequestParam(name = "submit", required = false) String actionType) throws ParseException {
		Integer tripId = Integer.parseInt((String) session.getAttribute("tripId"));
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		
		switch(actionType) {
			case "Inscrie-te la ascensiune": {
				if (this.isTripAvailableForUser(user, tripId)) {
					this.addTripForUser(user, tripId);
					this.decreaseTripCapacity(tripId);
					return "redirect:/my-trips";
				}
				else {
					model.addAttribute("isUserAllowedToRegisterForTrip", false);
					return "views/all/tripDetails";
				}	
			}
			case "Toate ascensiunile": {
				return "redirect:/all-trips";
			}
			case "Elimina ascensiunea": {
				this.removeTripForUser(principal, tripId);
				this.increaseTripCapacity(tripId);
				return "redirect:/my-trips";
			}
		}	
		return "views/all/tripDetails";
	}
	
	private void decreaseTripCapacity(Integer tripId) {
		Trip trip = tripRepository.findById(tripId).get();
		if(trip.getCapacity() > 0) {
			trip.setCapacity(trip.getCapacity() - 1);
			tripRepository.save(trip);
		}
	}
	
	private void increaseTripCapacity(Integer tripId) {
		Trip trip = tripRepository.findById(tripId).get();
			trip.setCapacity(trip.getCapacity() + 1);
			tripRepository.save(trip);
	}

	private TripDTO getTripDTOById(Integer tripId) {
		TripDTO tripDTO = null;
		for (Trip trip : tripRepository.findAll()) {
			if (trip.getId() == tripId) {
				tripDTO = TripUtils.convertFromTripToTripDTO(trip);
			}
		}
		return tripDTO;
	}

	private void addTripForUser(UserEntity user, Integer tripId){
		List<Trip> trips = user.getTrips();
		trips.add(tripRepository.findById(tripId).get());
		user.setTrips(trips);

		userRepository.save(user);
	}
	
	private void removeTripForUser(Principal principal, Integer tripId) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		List<Trip> userTrips = user.getTrips();
		
		if(!userTrips.isEmpty()) {
			userTrips.remove(tripRepository.findById(tripId).get());
			user.setTrips(userTrips);
			userRepository.save(user);
		}
	}

	private boolean isUserRegisteredForTrip(UserEntity user, Integer tripId) {
		for (Trip trip : user.getTrips()) {
			if (trip.getId() == tripId) {
				return true;
			}
		}
		return false;
	}

	private boolean isTripAvailableForUser(UserEntity user, Integer tripId) throws ParseException {
		Trip selectedTrip = tripRepository.findById(tripId).get();
		Date selectedTripStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTrip.getStartDate());
		Date selectedTripEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTrip.getEndDate());

		for (Trip trip : user.getTrips()) {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(trip.getStartDate());
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(trip.getEndDate());

			if (selectedTripStartDate.after(startDate) && selectedTripStartDate.before(endDate)
					|| selectedTripEndDate.after(startDate) && selectedTripEndDate.before(endDate)) {
				return false;
			}
		}
		return true;
	}
}
