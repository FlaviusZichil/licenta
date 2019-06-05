package app.dto;

import java.util.List;
import app.entities.UserTrip;

public class TripDTO {
	
	private Integer id;
	private Integer capacity;
	private String startDate;
	private String endDate;
	private String status;
	private Integer points;
	private List<UserTrip> users;
    private RouteDTO routeDTO;
    private PeakDTO peakDTO;
    private GuideDTO guideDTO;
    
	public TripDTO(Integer id, Integer capacity, String startDate, String endDate, String status, Integer points,
			List<UserTrip> users, RouteDTO routeDTO, PeakDTO peakDTO, GuideDTO guideDTO) {
		super();
		this.id = id;
		this.capacity = capacity;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.points = points;
		this.users = users;
		this.routeDTO = routeDTO;
		this.peakDTO = peakDTO;
		this.guideDTO = guideDTO;
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
	public List<UserTrip> getUsers() {
		return users;
	}

	public void setUsers(List<UserTrip> users) {
		this.users = users;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}
	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}
	public PeakDTO getPeakDTO() {
		return peakDTO;
	}
	public void setPeakDTO(PeakDTO peakDTO) {
		this.peakDTO = peakDTO;
	}
	public GuideDTO getGuideDTO() {
		return guideDTO;
	}
	public void setGuideDTO(GuideDTO guideDTO) {
		this.guideDTO = guideDTO;
	}
    
	
}
