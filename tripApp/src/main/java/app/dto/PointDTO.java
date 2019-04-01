package app.dto;

public class PointDTO {

	private Integer pointId;
	private String pointName;
		
	public PointDTO(Integer pointId, String pointName) {
		super();
		this.pointId = pointId;
		this.pointName = pointName;
	}
	
	public Integer getPointId() {
		return pointId;
	}
	public void setPointId(Integer pointId) {
		this.pointId = pointId;
	}
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
}
