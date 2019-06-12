package app.dto;

import app.entities.UserEntity;

public class GuideDTO {

	private Integer guideId;
	private Integer yearsOfExperience;
	private String phoneNumber;
	private String description;
	private Integer finishedTrips;
	private UserEntity user;

	public GuideDTO() {}
	
	public GuideDTO(Integer yearsOfExperience, String phoneNumber, UserEntity user) {
		super();
		this.yearsOfExperience = yearsOfExperience;
		this.phoneNumber = phoneNumber;
		this.user = user;
	}

	public GuideDTO(Integer guideId, UserEntity user, Integer yearsOfExperience, String phoneNumber, String description) {
		super();
		this.guideId = guideId;
		this.yearsOfExperience = yearsOfExperience;
		this.phoneNumber = phoneNumber;
		this.user = user;
		this.description = description;
	}

	public Integer getGuideId() {
		return guideId;
	}

	public void setGuideId(Integer guideId) {
		this.guideId = guideId;
	}

	public UserEntity getUser() {
		return user;
	}

	public Integer getFinishedTrips() {
		return finishedTrips;
	}

	public void setFinishedTrips(Integer finishedTrips) {
		this.finishedTrips = finishedTrips;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
