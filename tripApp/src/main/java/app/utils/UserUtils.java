package app.utils;

import java.util.ArrayList;
import java.util.List;

import app.dto.CityDTO;
import app.dto.PromoCodeDTO;
import app.dto.RoleDTO;
import app.dto.TripDTO;
import app.dto.UserDTO;
import app.entities.Trip;
import app.entities.UserEntity;

public class UserUtils {

	public static UserDTO convertFromUserToUserDTO(UserEntity user) {
		CityDTO cityDTO = new CityDTO(user.getCity().getName(), 
									  user.getCity().getLatitude(),
									  user.getCity().getLongitude());
		RoleDTO roleDTO = new RoleDTO(user.getRole().getName());
		PromoCodeDTO promoCodeDTO = new PromoCodeDTO(user.getPromoCode().getPromoCodeId(),
													 user.getPromoCode().getValue(),
													 user.getPromoCode().getStatus());
		UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(),
									  user.getBirthDate(), user.getPoints(), cityDTO, 
									  user.getEmail(), user.getExperience(), getAllTripsDTOForUser(user),
									  roleDTO, promoCodeDTO);
		return userDTO;
	}
	
	private static List<TripDTO> getAllTripsDTOForUser(UserEntity currentUser){
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : currentUserTrips) {
			TripDTO tripDTO = TripUtils.convertFromTripToTripDTO(trip);
			currentUserTripsDTO.add(tripDTO);
		}
		return currentUserTripsDTO;
	}
}
