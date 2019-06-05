package app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_trip")
public class UserTrip {

	@Id
	@Column(name = "user_trip_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userTripId;
	
	@ManyToOne
    @JoinColumn
    private UserEntity user;
	
	@ManyToOne
    @JoinColumn
    private Trip trip;
	
	@Column(name = "participated")
	private boolean participated;
	
	public UserTrip() {}

	public UserTrip(UserEntity user, Trip trip, boolean participated) {
		super();
		this.user = user;
		this.trip = trip;
		this.participated = participated;
	}

	public Integer getUserTripId() {
		return userTripId;
	}

	public void setUserTripId(Integer userTripId) {
		this.userTripId = userTripId;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public boolean isParticipated() {
		return participated;
	}

	public void setParticipated(boolean participated) {
		this.participated = participated;
	}

	@Override
	public String toString() {
		return "UserTrip [userTripId=" + userTripId + ", user=" + user + ", trip=" + trip + ", participated="
				+ participated + "]";
	}
}
