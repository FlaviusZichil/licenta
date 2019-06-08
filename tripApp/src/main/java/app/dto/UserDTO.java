package app.dto;

import java.util.List;

import app.entities.Register;

public class UserDTO {
	
	private Integer id;
	private String firstName;
	private String lastName;
	private String birthDate;
	private String points;
	private CityDTO cityDTO;
	private String email;
	private String experience;
	private List<TripDTO> tripsDTO;
	private RoleDTO roleDTO;
	private PromoCodeDTO promoCodeDTO;
	private Register register;
	private String description;
	private Boolean blocked;
	
	public UserDTO() {}
	
	public UserDTO(Integer id, String firstName, String lastName, String birthDate, String points, CityDTO cityDTO,
			String email, String experience, List<TripDTO> tripsDTO, RoleDTO roleDTO, PromoCodeDTO promoCodeDTO, Register register, Boolean blocked) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.points = points;
		this.cityDTO = cityDTO;
		this.email = email;
		this.experience = experience;
		this.tripsDTO = tripsDTO;
		this.roleDTO = roleDTO;
		this.promoCodeDTO = promoCodeDTO;
		this.register = register;
		this.blocked = blocked;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public CityDTO getCityDTO() {
		return cityDTO;
	}
	public void setCityDTO(CityDTO cityDTO) {
		this.cityDTO = cityDTO;
	}
	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public List<TripDTO> getTripsDTO() {
		return tripsDTO;
	}
	public void setTripsDTO(List<TripDTO> tripsDTO) {
		this.tripsDTO = tripsDTO;
	}
	public RoleDTO getRoleDTO() {
		return roleDTO;
	}
	public void setRoleDTO(RoleDTO roleDTO) {
		this.roleDTO = roleDTO;
	}
	public PromoCodeDTO getPromoCodeDTO() {
		return promoCodeDTO;
	}
	public void setPromoCodeDTO(PromoCodeDTO promoCodeDTO) {
		this.promoCodeDTO = promoCodeDTO;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate
				+ ", points=" + points + ", cityDTO=" + cityDTO + ", email=" + email + ", experience=" + experience
				+ ", tripsDTO=" + tripsDTO + ", roleDTO=" + roleDTO + ", promoCodeDTO=" + promoCodeDTO + ", register="
				+ register + ", description=" + description + ", blocked=" + blocked + "]";
	}
	
}
