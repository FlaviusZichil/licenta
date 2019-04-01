package app.dto;

import app.entities.UserEntity;

public class GuideDTO {

	private Integer guideId;
	private Integer yearsOfExperience;
	private String phoneNumber;
	private UserEntity user;

	public GuideDTO(Integer guideId, UserEntity user, Integer yearsOfExperience, String phoneNumber) {
		super();
		this.guideId = guideId;
		this.yearsOfExperience = yearsOfExperience;
		this.phoneNumber = phoneNumber;
		this.user = user;
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

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
