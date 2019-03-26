package app.dto;

public class CityDTO {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CityDTO [name=" + name + "]";
	}

	public CityDTO() {}
	
	public CityDTO(String name) {
		super();
		this.name = name;
	}	
}
