package app.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "trip")
public class Trip {

	@Id
	@Column(name = "trip_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "capacity")
	private Integer capacity;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;

	@Column(name = "status")
	private String status;

	@Column(name = "points")
	private Integer points;
	
	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<UserTrip> userTrips;

	@ManyToOne
	@JoinColumn
	private Guide guide;

	@ManyToOne
	@JoinColumn
	private Route route;

	@ManyToOne
	@JoinColumn
	private Peak peak;
	
	public Trip() {}

	public Trip(Integer capacity, String startDate, String endDate, String status, Integer points,
			Guide guide, Route route, Peak peak) {
		super();
		this.capacity = capacity;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.points = points;
		this.guide = guide;
		this.route = route;
		this.peak = peak;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public List<UserTrip> getUserTrips() {
		return userTrips;
	}

	public void setUserTrips(List<UserTrip> userTrips) {
		this.userTrips = userTrips;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Peak getPeak() {
		return peak;
	}

	public void setPeak(Peak peak) {
		this.peak = peak;
	}

	@Override
	public String toString() {
		return "Trip [capacity=" + capacity + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status
				+ ", points=" + points + ", difficulty=" + "]";
	}
}
