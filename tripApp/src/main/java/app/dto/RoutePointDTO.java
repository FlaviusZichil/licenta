package app.dto;

public class RoutePointDTO {

	private Integer routePointId;
	private String order;
	private PointDTO pointDTO;
	
	public RoutePointDTO(Integer routePointId, String order, PointDTO pointDTO) {
		super();
		this.routePointId = routePointId;
		this.order = order;
		this.pointDTO = pointDTO;
	}
	public Integer getRoutePointId() {
		return routePointId;
	}
	public void setRoutePointId(Integer routePointId) {
		this.routePointId = routePointId;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public PointDTO getPointDTO() {
		return pointDTO;
	}
	public void setPointDTO(PointDTO pointDTO) {
		this.pointDTO = pointDTO;
	}
}
