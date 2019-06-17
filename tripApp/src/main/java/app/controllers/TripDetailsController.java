package app.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import app.dto.UserDTO;
import app.entities.Medal;
import app.entities.Trip;
import app.entities.UserEntity;
import app.entities.UserMedal;
import app.entities.UserTrip;
import app.models.TripDetailsViewModel;
import app.repositories.MedalRepository;
import app.repositories.TripRepository;
import app.repositories.UserMedalRepository;
import app.repositories.UserRepository;
import app.repositories.UserTripRepository;
import app.utils.Conversion;
import app.utils.UserUtils;

@Controller
public class TripDetailsController {
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private Conversion conversion;	
	@Autowired
	private UserTripRepository userTripRepository;	
	@Autowired
	private MedalRepository medalRepository;
	@Autowired
	private UserMedalRepository userMedalRepository;
	
	@SessionScope
	@GetMapping("/trip-details")
	public String getTripDetails(Model model, Principal principal, TripDetailsViewModel tripDetailsViewModel,
			HttpSession session, @RequestParam(value = "trip", required = false) String tripId) {
		
		model.addAttribute("noTripSelected", false);
		if(tripId == null) {
			model.addAttribute("noTripSelected", true);
			return "views/all/tripDetails";
		}
		tripDetailsViewModel.setTripDTO(getTripDTOById(Integer.parseInt(tripId)));
		model.addAttribute("tripDetailsViewModel", tripDetailsViewModel);

		UserEntity user = userUtils.getUserByEmail(principal.getName());
		if (isUserRegisteredForTrip(user, Integer.parseInt(tripId))) {
			model.addAttribute("isAlreadyRegisteredForTrip", true);
		} else {
			model.addAttribute("isAlreadyRegisteredForTrip", false);
		}
		
		model.addAttribute("participants", getParticipantsForTrip(getTripById(Integer.parseInt(tripId))));

		session.setAttribute("tripId", tripId);
		return "views/all/tripDetails";
	}

	@PostMapping("/trip-details")
	public String postTripDetails(HttpSession session, Principal principal, Model model,
									@RequestParam(name = "submit", required = false) String actionType,
									@RequestParam(name = "participated", required = false) String userId) throws ParseException {
		Integer tripId = Integer.parseInt((String) session.getAttribute("tripId"));
		UserEntity user = userUtils.getUserByEmail(principal.getName());

		switch(actionType) {
			case "Înscrie-te la ascensiune": {
				if(isUserRegisteredForTrip(user, tripId)) {
					return "redirect:/my-trips"; 
				}
				if (isTripAvailableForUser(user, tripId)) {
					addTripForUser(user, tripId);
					decreaseTripCapacity(tripId);
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
				if(!isUserRegisteredForTrip(user, tripId)) {
					return "redirect:/my-trips"; 
				}
				removeTripForUser(principal, tripId);
				increaseTripCapacity(tripId);
				return "redirect:/my-trips";
			}
			case "Sterge ascensiunea": {
				return "redirect:/my-trips";
			}
			case "Finalizeaza":{
				setStatusToFinishedForTrip(getTripById(tripId));
				setUsersThatParticipated(userId, tripId);
				addMedalsForUsers(userId, tripId);
				return "redirect:/my-trips";
			}
		}	
		return "views/all/tripDetails";
	}
	
	private void addMedalsForUsers(String userId, Integer tripId) {
		List<String> usersIds = new ArrayList<>(Arrays.asList(userId.split(",")));
		for(String id : usersIds) {
			UserEntity user = userUtils.getUserById(Integer.parseInt(id));
			Trip trip = getTripById(tripId);
			Medal medal = getMedatByTrip(trip);
			if(!doesUserHasThatMedalAlready(user, medal)) {
				userMedalRepository.save(new UserMedal(medal, user));
			}
		}	
	}
	
	private boolean doesUserHasThatMedalAlready(UserEntity user, Medal medal) {
		for(UserMedal userMedal : userMedalRepository.findAll()) {
			if(userMedal.getUser().equals(user) && userMedal.getMedal().equals(medal)) {
				return true;
			}
		}
		return false;
	}

	private Medal getMedatByTrip(Trip trip) {
		for(Medal medal : medalRepository.findAll()) {
			if(medal.getPeak().equals(trip.getPeak())) {
				return medal;
			}
		}
		return null;
	}

	private void setUsersThatParticipated(String userId, Integer tripId) {
		List<String> usersIds = new ArrayList<>(Arrays.asList(userId.split(",")));
		for(String id : usersIds) {
			UserEntity user = userUtils.getUserById(Integer.parseInt(id));
			Trip trip = getTripById(tripId);
			UserTrip userTrip = getUserTrip(user, trip);
			userTrip.setParticipated(true);
			userTripRepository.save(userTrip);
		}
	}

	private void setStatusToFinishedForTrip(Trip trip) {
		trip.setStatus("Finished");
		tripRepository.save(trip);
	}

	private List<UserDTO> getParticipantsForTrip(Trip trip){
		List<UserDTO> participants = new ArrayList<>();
		for(UserTrip userTrip : trip.getUserTrips()) {
			participants.add(userUtils.convertFromUserToUserDTO(userTrip.getUser()));
		}
		return participants;
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
				tripDTO = conversion.convertFromTripToTripDTO(trip);
			}
		}
		return tripDTO;
	}

	private void addTripForUser(UserEntity user, Integer tripId){
		List<UserTrip> userTrips = user.getUserTrips();
		userTrips.add(new UserTrip(user, getTripById(tripId), false));
		user.setUserTrips(userTrips);
		userRepository.save(user);
	}
	
	private void removeTripForUser(Principal principal, Integer tripId) {
		UserEntity user = userUtils.getUserByEmail(principal.getName());
		Trip trip = getTripById(tripId);
		userTripRepository.delete(getUserTrip(user, trip));
	}
		
	private UserTrip getUserTrip(UserEntity user, Trip trip) {
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getTrip().equals(trip) && userTrip.getUser().equals(user)) {
				return userTrip;
			}
		}
		return null;
	}
	
	private boolean isUserRegisteredForTrip(UserEntity user, Integer tripId) {
		Trip trip = getTripById(tripId);
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getUser().equals(user) && userTrip.getTrip().equals(trip)) {
				return true;
			}
		}
		return false;
	}
	
	private Trip getTripById(Integer tripId) {
		for(Trip trip : tripRepository.findAll()) {
			if(trip.getId() == tripId) {
				return trip;
			}
		}
		return null;
	}

	private boolean isTripAvailableForUser(UserEntity user, Integer tripId) throws ParseException {
		Trip selectedTrip = tripRepository.findById(tripId).get();
		Date selectedTripStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTrip.getStartDate());
		Date selectedTripEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTrip.getEndDate());

		for (UserTrip userTrip : user.getUserTrips()) {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(userTrip.getTrip().getStartDate());
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(userTrip.getTrip().getEndDate());

			if (selectedTripStartDate.after(startDate) && selectedTripStartDate.before(endDate)
					|| selectedTripEndDate.after(startDate) && selectedTripEndDate.before(endDate)) {
				return false;
			}
		}
		return true;
	}
}
