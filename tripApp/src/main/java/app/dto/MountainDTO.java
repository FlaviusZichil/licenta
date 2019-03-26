package app.dto;

public class MountainDTO {
	
	private String name;
	private CityDTO cityDTO;
	
	public MountainDTO(String name, CityDTO cityDTO) {
		super();
		this.name = name;
		this.cityDTO = cityDTO;
	}
	
	public MountainDTO() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CityDTO getCityDTO() {
		return cityDTO;
	}
	public void setCityDTO(CityDTO cityDTO) {
		this.cityDTO = cityDTO;
	}
	
	@Override
	public String toString() {
		return "MountainDTO [name=" + name + "]";
	}
}
