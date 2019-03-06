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
	
	@Column(name = "difficulty")
	private String difficulty;
	
	@ManyToOne
    @JoinColumn
    private Route route;
	
	@ManyToOne
    @JoinColumn
    private Peak peak;

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

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
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
				+ ", points=" + points + ", difficulty=" + difficulty + "]";
	}
}
