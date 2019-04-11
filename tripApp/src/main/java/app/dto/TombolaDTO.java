package app.dto;

public class TombolaDTO {

	private String date;
	private UserDTO firstPlaceWinner;
	private UserDTO secondPlaceWinner;
	private UserDTO thirdPlaceWinner;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public UserDTO getFirstPlaceWinner() {
		return firstPlaceWinner;
	}
	public void setFirstPlaceWinner(UserDTO firstPlaceWinner) {
		this.firstPlaceWinner = firstPlaceWinner;
	}
	public UserDTO getSecondPlaceWinner() {
		return secondPlaceWinner;
	}
	public void setSecondPlaceWinner(UserDTO secondPlaceWinner) {
		this.secondPlaceWinner = secondPlaceWinner;
	}
	public UserDTO getThirdPlaceWinner() {
		return thirdPlaceWinner;
	}
	public void setThirdPlaceWinner(UserDTO thirdPlaceWinner) {
		this.thirdPlaceWinner = thirdPlaceWinner;
	}
	@Override
	public String toString() {
		return "TombolaModel [date=" + date + ", firstPlaceWinner=" + firstPlaceWinner + ", secondPlaceWinner="
				+ secondPlaceWinner + ", thirdPlaceWinner=" + thirdPlaceWinner + "]";
	}
}
