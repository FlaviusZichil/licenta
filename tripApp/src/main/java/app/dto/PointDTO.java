package app.dto;

public class PointDTO {

	private Integer pointId;
	private String pointName;
	private MountainDTO mountainDTO;
		
	public PointDTO(Integer pointId, String pointName, MountainDTO mountainDTO) {
		super();
		this.pointId = pointId;
		this.pointName = pointName;
		this.mountainDTO = mountainDTO;
	}
	
	public PointDTO(Integer pointId, String pointName) {
		super();
		this.pointId = pointId;
		this.pointName = pointName;
	}
	
	public MountainDTO getMountainDTO() {
		return mountainDTO;
	}

	public void setMountainDTO(MountainDTO mountainDTO) {
		this.mountainDTO = mountainDTO;
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

	@Override
	public String toString() {
		return "PointDTO [pointId=" + pointId + ", pointName=" + pointName + ", mountainDTO=" + mountainDTO + "]";
	}
}
