package app.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import app.entities.Mountain;
import app.entities.Peak;
import app.entities.Trip;

public class PeakDTO {

	private Integer id;
	private String peakName;
	private Integer altitude;
	private CityDTO cityDTO;
    private MountainDTO mountainDTO;
    private List<Trip> trips;
        
	public PeakDTO(Integer id, String peakName, Integer altitude, CityDTO cityDTO, MountainDTO mountainDTO,
			List<Trip> trips) {
		super();
		this.id = id;
		this.peakName = peakName;
		this.altitude = altitude;
		this.cityDTO = cityDTO;
		this.mountainDTO = mountainDTO;
		this.trips = trips;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPeakName() {
		return peakName;
	}
	public void setPeakName(String peakName) {
		this.peakName = peakName;
	}
	public Integer getAltitude() {
		return altitude;
	}
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}
	public CityDTO getCityDTO() {
		return cityDTO;
	}
	public void setCityDTO(CityDTO cityDTO) {
		this.cityDTO = cityDTO;
	}
	public MountainDTO getMountainDTO() {
		return mountainDTO;
	}
	public void setMountainDTO(MountainDTO mountainDTO) {
		this.mountainDTO = mountainDTO;
	}
	public List<Trip> getTrips() {
		return trips;
	}
	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}

	@Override
	public String toString() {
		return "PeakDTO [id=" + id + ", peakName=" + peakName + ", altitude=" + altitude + ", cityDTO=" + cityDTO
				+ ", mountainDTO=" + mountainDTO + "]";
	}
}
