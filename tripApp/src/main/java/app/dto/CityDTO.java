package app.dto;

public class CityDTO {
	
	private String name;
	private Double latitude;
	private Double longitude;

	public CityDTO() {}
	
	public CityDTO(String name) {
		super();
		this.name = name;
	}

	public CityDTO(String name, Double latitude, Double longitude) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "CityDTO [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}	
}
