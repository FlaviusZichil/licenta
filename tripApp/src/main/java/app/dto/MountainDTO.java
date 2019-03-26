package app.dto;

public class MountainDTO {
	
	private String name;
	
	public MountainDTO(String name) {
		super();
		this.name = name;
	}
	
	public MountainDTO() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
