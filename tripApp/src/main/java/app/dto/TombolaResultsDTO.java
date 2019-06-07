package app.dto;

public class TombolaResultsDTO {

	private UserDTO userDTO;
	private String date;
	private String status;
	
	public TombolaResultsDTO() {}
	
	public TombolaResultsDTO(UserDTO userDTO, String date, String status) {
		super();
		this.userDTO = userDTO;
		this.date = date;
		this.status = status;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TombolaResultsDTO [userDTO=" + userDTO + ", date=" + date + ", status=" + status + "]";
	}
}
