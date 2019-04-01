package app.dto;

import java.util.List;

public class RouteDTO {
	
	private Integer routeId;
	private String difficulty;
	private List<RoutePointDTO> routePointsDTO;
	
	public RouteDTO(Integer routeId, String difficulty, List<RoutePointDTO> routePointsDTO) {
		super();
		this.routeId = routeId;
		this.difficulty = difficulty;
		this.routePointsDTO = routePointsDTO;
	}
	
	public Integer getRouteId() {
		return routeId;
	}
	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	public List<RoutePointDTO> getRoutePointsDTO() {
		return routePointsDTO;
	}
	public void setRoutePointsDTO(List<RoutePointDTO> routePointsDTO) {
		this.routePointsDTO = routePointsDTO;
	}
}
