package app.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import app.entities.Peak;
import app.entities.Route;
import app.entities.Trip;
import app.entities.UserEntity;

public class TripDTO {
	
	private Integer id;
	private Integer capacity;
	private String startDate;
	private String endDate;
	private String status;
	private Integer points;
	private String difficulty;
	private List<UserEntity> users;
    private Route route;
    private PeakDTO peakDTO;
    
	public TripDTO(Integer id, Integer capacity, String startDate, String endDate, String status, Integer points,
			String difficulty, List<UserEntity> users, Route route, PeakDTO peakDTO) {
		super();
		this.id = id;
		this.capacity = capacity;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.points = points;
		this.difficulty = difficulty;
		this.users = users;
		this.route = route;
		this.peakDTO = peakDTO;
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

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public PeakDTO getPeakDTO() {
		return peakDTO;
	}

	public void setPeakDTO(PeakDTO peakDTO) {
		this.peakDTO = peakDTO;
	}

	@Override
	public String toString() {
		return "TripDTO [id=" + id + ", capacity=" + capacity + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", status=" + status + ", points=" + points + ", difficulty=" + difficulty + ", route=" + route
				+ ", peakDTO=" + peakDTO + "]";
	}	
}
