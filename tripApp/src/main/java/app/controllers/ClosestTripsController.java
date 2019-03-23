package app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import app.entities.City;
import app.entities.Trip;
import app.entities.UserEntity;
import app.repositories.TripRepository;
import app.repositories.UserRepository;

@Controller
public class ClosestTripsController {
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/closest-trips")
	public String getClosestTrips(Principal principal){
		List<Trip> trips = this.sortTripsByDistance(principal);		
		return "views/user/userMainPage";
	}
	
	private Double calculateDistanceBetweenTwoCities(City firstCity, City secondCity) {
		if ((firstCity.getLatitude() == secondCity.getLatitude()) && (firstCity.getLongitude() == secondCity.getLongitude())) {
			return 0.0;
		}
		else {
			double theta = firstCity.getLongitude() - secondCity.getLongitude();
			double distance = Math.sin(Math.toRadians(firstCity.getLatitude())) * Math.sin(Math.toRadians(secondCity.getLatitude())) + 
							  Math.cos(Math.toRadians(firstCity.getLatitude())) * Math.cos(Math.toRadians(secondCity.getLatitude())) * 
							  Math.cos(Math.toRadians(theta));
			distance = Math.acos(distance);
			distance = Math.toDegrees(distance);
			distance = distance * 60 * 1.1515;
			distance = distance * 1.609344;

			return distance;
		}
	}
	
	private List<Trip> sortTripsByDistance(Principal principal){
		// trebuie luate doar cele active
		Iterable<Trip> allTrips = tripRepository.findAll();
		List<Trip> trips = this.convertFromIterableToList(allTrips);
		final UserEntity user = this.getUserByEmail(principal.getName());
		
		Collections.sort(trips, new Comparator<Trip>(){
			   @Override
			   public int compare(Trip firstTrip, Trip secondTrip) {
				   Double firstDistance = calculateDistanceBetweenTwoCities(user.getCity(), firstTrip.getPeak().getCity());
				   Double secondDistance = calculateDistanceBetweenTwoCities(user.getCity(), secondTrip.getPeak().getCity());
				   
				   if(firstDistance > secondDistance) {
					   return 1;
				   }
				   
				   if(firstDistance < secondDistance) {
					   return -1;
				   }
				   return 0;
			     }
			 });
		
		return trips;
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
	
	private List<Trip> convertFromIterableToList(Iterable<Trip> trips){
		List<Trip> tripsList = new ArrayList<>();
		for(Trip trip : trips) {
			tripsList.add(trip);
		}
		return tripsList;
	}
}
