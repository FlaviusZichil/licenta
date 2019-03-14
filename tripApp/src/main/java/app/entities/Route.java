package app.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "route")
public class Route {
	
	@Id
	@Column(name = "route_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "start_point")
	private String startPoint;
	
	@Column(name = "intermediate_point")
	private String intermediatePoint;
	
	@Column(name = "end_point")
	private String endPoint;
	
	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<Trip> trips;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	public String getIntermediatePoint() {
		return intermediatePoint;
	}

	public void setIntermediatePoint(String intermediatePoint) {
		this.intermediatePoint = intermediatePoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}

	@Override
	public String toString() {
		return "Route [startPoint=" + startPoint + ", intermediatePoint=" + intermediatePoint + ", endPoint=" + endPoint
				+ "]";
	}
}
