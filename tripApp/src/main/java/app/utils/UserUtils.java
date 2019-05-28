package app.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.dto.CityDTO;
import app.dto.PromoCodeDTO;
import app.dto.RoleDTO;
import app.dto.TripDTO;
import app.dto.UserDTO;
import app.entities.Trip;
import app.entities.UserEntity;
import app.repositories.UserRepository;

@Component
public class UserUtils {
	
	@Autowired
	private UserRepository userRepository;

	public UserDTO convertFromUserToUserDTO(UserEntity user) {
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
									  roleDTO, promoCodeDTO, user.getRegister());
		return userDTO;
	}
	
	public List<TripDTO> getAllTripsDTOForUser(UserEntity currentUser){
		List<Trip> currentUserTrips = currentUser.getTrips();
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();
		
		for(Trip trip : currentUserTrips) {
			TripDTO tripDTO = TripUtils.convertFromTripToTripDTO(trip);
			currentUserTripsDTO.add(tripDTO);
		}
		return currentUserTripsDTO;
	}
	
	public UserEntity getUserById(Integer userId) {
		for(UserEntity user : userRepository.findAll()) {
			if(user.getId() == userId) {
				return user;
			}
		}
		return null;
	}
	
	public UserEntity getUserByEmail(String email) {	
		for(UserEntity user : userRepository.findAll()) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
}
