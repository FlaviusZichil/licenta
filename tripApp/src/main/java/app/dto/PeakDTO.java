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
	private String city;
    private Mountain mountain;
    private List<Trip> trips;
    
	public PeakDTO(Integer id, String peakName, Integer altitude, String city, List<Trip> trips, Mountain mountain) {
		super();
		this.id = id;
		this.peakName = peakName;
		this.altitude = altitude;
		this.city = city;
		this.trips = trips;
		this.mountain = mountain;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<Trip> getTrips() {
		return trips;
	}
	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}
	public Mountain getMountain() {
		return mountain;
	}
	public void setMountain(Mountain mountain) {
		this.mountain = mountain;
	}
    
    
	
//	private Peak peak;
//	private MountainDTO mountainDTO;
//	
//	public Peak getPeak() {
//		return peak;
//	}
//	public void setPeak(Peak peak) {
//		this.peak = peak;
//	}
//	public MountainDTO getMountainDTO() {
//		return mountainDTO;
//	}
//	public void setMountainDTO(MountainDTO mountainDTO) {
//		this.mountainDTO = mountainDTO;
//	}
//	
//	@Override
//	public String toString() {
//		return "PeakDTO [peak=" + peak + ", mountainDTO=" + mountainDTO + "]";
//	}
}
