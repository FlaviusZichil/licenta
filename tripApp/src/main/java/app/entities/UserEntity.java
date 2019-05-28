package app.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {
	
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "birth_date")
	private String birthDate;
	
	@Column(name = "points")
	private String points;
	
	@ManyToOne
    @JoinColumn
    private City city;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "experience")
	private String experience;
	
	@Column(name = "password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_trip",
	     	   joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
	           inverseJoinColumns = @JoinColumn(name = "trip_id", referencedColumnName = "trip_id"))
	private List<Trip> trips;
	
	@ManyToOne
    @JoinColumn
    private Role role;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "promo_code_id", referencedColumnName = "promo_code_id")
    private PromoCode promoCode;
	
	@OneToOne(mappedBy = "user")
    private Register register;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMedal> medals;
	
    @OneToOne(mappedBy = "user")
    private Guide guide;
    
    @OneToMany(mappedBy = "userTombola", cascade = CascadeType.ALL)
    private List<Tombola> tombola;
    
    public UserEntity() {}

	public UserEntity(String firstName, String lastName, String birthDate, String email, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(PromoCode promoCode) {
		this.promoCode = promoCode;
	}

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public List<UserMedal> getMedals() {
		return medals;
	}

	public void setMedals(List<UserMedal> medals) {
		this.medals = medals;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}

	public List<Tombola> getTombola() {
		return tombola;
	}

	public void setTombola(List<Tombola> tombola) {
		this.tombola = tombola;
	}

	@Override
	public String toString() {
		return "UserEntity [firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate
				+ ", points=" + points + ", email=" + email + ", experience=" + experience + ", password=" + password
				+ ", role=" + role + ", promoCode=" + promoCode + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (experience == null) {
			if (other.experience != null)
				return false;
		} else if (!experience.equals(other.experience))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (guide == null) {
			if (other.guide != null)
				return false;
		} else if (!guide.equals(other.guide))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (promoCode == null) {
			if (other.promoCode != null)
				return false;
		} else if (!promoCode.equals(other.promoCode))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (tombola == null) {
			if (other.tombola != null)
				return false;
		} else if (!tombola.equals(other.tombola))
			return false;
		if (trips == null) {
			if (other.trips != null)
				return false;
		} else if (!trips.equals(other.trips))
			return false;
		return true;
	}
}
