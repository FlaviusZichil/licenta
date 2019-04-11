package app.dto;

import app.entities.UserEntity;

public class TombolaDTO {

	private String date;
	private UserEntity firstPlaceWinner;
	private UserEntity secondPlaceWinner;
	private UserEntity thirdPlaceWinner;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public UserEntity getFirstPlaceWinner() {
		return firstPlaceWinner;
	}
	public void setFirstPlaceWinner(UserEntity firstPlaceWinner) {
		this.firstPlaceWinner = firstPlaceWinner;
	}
	public UserEntity getSecondPlaceWinner() {
		return secondPlaceWinner;
	}
	public void setSecondPlaceWinner(UserEntity secondPlaceWinner) {
		this.secondPlaceWinner = secondPlaceWinner;
	}
	public UserEntity getThirdPlaceWinner() {
		return thirdPlaceWinner;
	}
	public void setThirdPlaceWinner(UserEntity thirdPlaceWinner) {
		this.thirdPlaceWinner = thirdPlaceWinner;
	}
	@Override
	public String toString() {
		return "TombolaModel [date=" + date + ", firstPlaceWinner=" + firstPlaceWinner + ", secondPlaceWinner="
				+ secondPlaceWinner + ", thirdPlaceWinner=" + thirdPlaceWinner + "]";
	}
}
