package app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.dto.ArticleDTO;
import app.dto.CityDTO;
import app.dto.PromoCodeDTO;
import app.dto.RoleDTO;
import app.dto.TripDTO;
import app.dto.UserDTO;
import app.entities.UserEntity;
import app.entities.UserTrip;
import app.repositories.UserRepository;

@Component
public class UserUtils {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Conversion conversion;
	
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
		List<TripDTO> currentUserTripsDTO = new ArrayList<TripDTO>();
		
		for(UserTrip userTrip : currentUser.getUserTrips()) {
			TripDTO tripDTO = conversion.convertFromTripToTripDTO(userTrip.getTrip());
			currentUserTripsDTO.add(tripDTO);
		}
		
		return sortTripsByStatus(currentUserTripsDTO);
	}
	
	public List<TripDTO> sortTripsByStatus(List<TripDTO> tripsDTO){
		Collections.sort(tripsDTO, new Comparator<TripDTO>(){
			   @Override
			   public int compare(TripDTO firstTripDTO, TripDTO secondTripDTO) {			   				   
				   if(firstTripDTO.getStatus().equals("Active") && secondTripDTO.getStatus().equals("Finished")) {
					   return -1;			   
				   }				   
				   if(firstTripDTO.getStatus().equals("Finished") && secondTripDTO.getStatus().equals("Active")) {
					   return 1;
				   }
				   return 0;
			     }
			 });
		return tripsDTO;
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
