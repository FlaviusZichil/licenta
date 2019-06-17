package app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.documents.Article;
import app.dto.CityDTO;
import app.dto.PromoCodeDTO;
import app.dto.RoleDTO;
import app.dto.TripDTO;
import app.dto.UserDTO;
import app.entities.UserEntity;
import app.entities.UserTrip;
import app.repositories.ArticleRepository;
import app.repositories.UserRepository;
import app.repositories.UserTripRepository;

@Component
public class UserUtils {	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private UserTripRepository userTripRepository;
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
									  roleDTO, promoCodeDTO, user.getRegister(), user.isBlocked());
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
	
	public Integer getFinishedTripsForUser(UserEntity user) {
		Integer finishedTrips = 0;
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getUser().equals(user) && userTrip.getTrip().getStatus().equals("Finished")) {
				finishedTrips++;
			}
		}
		return finishedTrips;
	}
	
	public Integer getAbsencesForUser(UserEntity user) {
		Integer absences = 0;
		for(UserTrip userTrip : userTripRepository.findAll()) {
			if(userTrip.getUser().equals(user) && !userTrip.isParticipated()) {
				absences++;
			}
		}
		return absences;
	}
	
	public Integer getArticleForUser(UserEntity user) {
		Integer articlesForUser = 0;
		for(Article article : articleRepository.findAll()) {
			if(article.getUserId() == user.getId()) {
				articlesForUser++;
			}
		}
		return articlesForUser;
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
