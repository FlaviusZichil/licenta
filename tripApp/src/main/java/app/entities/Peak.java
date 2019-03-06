package app.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "peak")
public class Peak {
	
	@Id
	@Column(name = "peak_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "peak_name")
	private String peakName;
	
	@Column(name = "altitude")
	private Integer altitude;
	
	@Column(name = "city")
	private String city;
	
	@OneToMany(mappedBy = "peak", cascade = CascadeType.ALL)
    private List<Trip> trips;
	
	@ManyToOne
    @JoinColumn
    private Mountain mountain;

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

	@Override
	public String toString() {
		return "Peak [peakName=" + peakName + ", altitude=" + altitude + ", city=" + city + "]";
	}
}
