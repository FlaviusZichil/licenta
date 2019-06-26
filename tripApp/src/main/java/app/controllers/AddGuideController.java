package app.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.dto.GuideDTO;
import app.entities.City;
import app.entities.Guide;
import app.entities.PromoCode;
import app.entities.Role;
import app.entities.UserEntity;
import app.repositories.CityRepository;
import app.repositories.GuideRepository;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import app.validators.RegisterValidator;

@Controller
public class AddGuideController {
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GuideRepository guideRepository;

	@GetMapping("/add-guide")
	public String getAddGuidePage(Model model) {
		model.addAttribute("allCities", getAllCitiesNames());
		model.addAttribute("guideDTO", new GuideDTO());
		return "views/admin/addGuideView";
	}

	@PostMapping("/add-guide")
	public String addGuideActions(Model model,
								  @RequestParam(name = "submit", required = false) String addGuideAction,
								  @RequestParam(name = "guideLastName", required = false) String guideLastName,
								  @RequestParam(name = "guideFirstName", required = false) String guideFirstName,
								  @RequestParam(name = "guideEmail", required = false) String guideEmail,
								  @RequestParam(name = "guidePassword", required = false) String guidePassword,
								  @RequestParam(name = "guideBirthDate", required = false) String guideBirthDate,
								  @RequestParam(name = "guideCity", required = false) String guideCity,
								  @RequestParam(name = "guidePhoneNumber", required = false) String guidePhoneNumber,
								  @RequestParam(name = "guideExperience", required = false) String guideExperience) {
		
		if (addGuideAction != null) {
			UserEntity user = new UserEntity();
			GuideDTO guideDTO = new GuideDTO();
			
			if(isLastNameValid(guideLastName)) {
				user.setLastName(RegisterValidator.formatNameProperly(guideLastName));
			}
			else {
				model.addAttribute("wrongLastName", true);
			}
			
			if(isFirstNameValid(guideFirstName)) {
				user.setFirstName(RegisterValidator.formatNameProperly(guideFirstName));
			}
			else {
				model.addAttribute("wrongFirstName", true);
			}
			
			if(isEmailValid(guideEmail)) {
				user.setEmail(guideEmail);
			}
			else {
				model.addAttribute("wrongEmail", true);
			}
			
			if(isPasswordValid(guidePassword)) {
				user.setPassword(guidePassword);
			}
			else {
				model.addAttribute("wrongPassword", true);
			}
			
			if(!isBirthDateValid(guideBirthDate)) {
				model.addAttribute("wrongBirthDate", true);			
			}

			if(isPhoneNumberValid(guidePhoneNumber)) {
				guideDTO.setPhoneNumber(guidePhoneNumber);
			}
			else {
				model.addAttribute("wrongPhoneNumber", true);
			}
			
			if(isExperienceValid(guideExperience, guideBirthDate)) {
				guideDTO.setYearsOfExperience(Integer.parseInt(guideExperience));
			}
			else {
				model.addAttribute("wrongYears", true);
			}
						
			if(isLastNameValid(guideLastName) && isFirstNameValid(guideFirstName) && isEmailValid(guideEmail) && isPasswordValid(guidePassword) && isBirthDateValid(guideBirthDate)) {
				user.setExperience("Avansat");
				user.setCity(getCityByName(guideCity));
				user.setRole(getRoleByName("ROLE_GUIDE"));
				user.setPoints("25");
				user.setBlocked(false);
				user.setBirthDate(guideBirthDate);
				PromoCode promoCodeForUser = new PromoCode(this.generatePromoCode(), "Active");		
				user.setPromoCode(promoCodeForUser);
				if(isPhoneNumberValid(guidePhoneNumber) && isExperienceValid(guideExperience, guideBirthDate)) {
					UserEntity savedUser = saveUser(user);
					Guide guide = new Guide(savedUser, Integer.parseInt(guideExperience), guidePhoneNumber);
					guideRepository.save(guide);
					model.addAttribute("addedWithSuccess", true);
					model.addAttribute("guideDTO", new GuideDTO());
				}
				else {
					if(isPhoneNumberValid(guidePhoneNumber)) {
						guideDTO.setPhoneNumber(guidePhoneNumber);
					}
					if(isExperienceValid(guideExperience, guideBirthDate)) {
						guideDTO.setYearsOfExperience(Integer.parseInt(guideExperience));
					}
					guideDTO.setUser(user);
					model.addAttribute("guideDTO", guideDTO);
				}		
			}
			else {
				model.addAttribute("addedWithSuccess", false);
				guideDTO.setUser(user);
				model.addAttribute("guideDTO", guideDTO);
			}
			model.addAttribute("allCities", getAllCitiesNames());
		}
		return "views/admin/addGuideView";
	}
	
	private Integer getAgeFromBirthDate(String birthDate) {
		LocalDate birth = LocalDate.parse(birthDate);
		Period period = Period.between(birth, LocalDate.now());
	    Integer years = period.getYears();
	    return years;
	}
	
	public String generatePromoCode() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 20) {
            int index = (int) (random.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
	}
	
	private UserEntity saveUser(UserEntity user) {
		userRepository.save(user);
		return user;
	}
	
	private boolean isEmailTaken(String email) {
		Iterable<UserEntity> allUsers = userRepository.findAll();
		for (UserEntity user : allUsers) {
			if (user.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}

	private boolean isLastNameValid(String guideLastName) {
		if(guideLastName != "" && RegisterValidator.isNameValid(guideLastName)) {
			return true;
		}
		return false;
	}
	
	private boolean isFirstNameValid(String guideFirstName) {
		if(guideFirstName != "" && RegisterValidator.isNameValid(guideFirstName)) {
			return true;
		}
		return false;
	}
	
	private boolean isEmailValid(String guideEmail) {
		if(guideEmail != "" && RegisterValidator.isEmailValid(guideEmail) && !isEmailTaken(guideEmail)) {
			return true;
		}
		return false;
	}
	
	private boolean isPasswordValid(String guidePassword) {
		if(guidePassword != "" && RegisterValidator.isPasswordValid(guidePassword)) {
			return true;
		}
		return false;
	}
	
	private boolean isBirthDateValid(String guideBirthDate) {
		if(guideBirthDate != "" && guideBirthDate != null) {
			if(RegisterValidator.isDateValid(guideBirthDate)) {
				return true;
			}		
		}
		return false;
	}
	
	private boolean isPhoneNumberValid(String guidePhoneNumber) {
		if(guidePhoneNumber != "" && guidePhoneNumber.length() >= 10 && guidePhoneNumber.length() < 15) {
			return true;
		}
		return false;
	}
	
	private boolean isExperienceValid(String guideExperience, String birthDate) {
		if(guideExperience != "" && birthDate != "" && birthDate != null) {
			if(Integer.parseInt(guideExperience) <= getAgeFromBirthDate(birthDate) - 18) {
				return true;
			}
		}
		return false;
	}

	private List<String> getAllCitiesNames() {
		List<String> cityNames = new ArrayList<>();
		for (City city : cityRepository.findAll()) {
			cityNames.add(city.getName());
		}
		Collections.sort(cityNames);
		return cityNames;
	}

	private City getCityByName(String cityName) {
		for (City city : cityRepository.findAll()) {
			if (city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}
	
	private Role getRoleByName(String roleName) {
		for (Role role : roleRepository.findAll()) {
			if (role.getName().equals(roleName)) {
				return role;
			}
		}
		return null;
	}
}
