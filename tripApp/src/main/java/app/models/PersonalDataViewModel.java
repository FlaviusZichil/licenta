package app.models;

import app.dto.UserDTO;

public class PersonalDataViewModel {

	private UserDTO userDTO;

	public PersonalDataViewModel() {
		super();
	}

	public PersonalDataViewModel(UserDTO userDTO) {
		super();
		this.userDTO = userDTO;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	@Override
	public String toString() {
		return "PersonalDataViewModel [userDTO=" + userDTO + "]";
	}
}
