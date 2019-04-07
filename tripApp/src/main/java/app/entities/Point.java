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
@Table(name = "point")
public class Point {

	@Id
	@Column(name = "point_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(mappedBy = "point", cascade = CascadeType.ALL)
	private List<RoutePoint> routePoints;

	@Column(name = "name")
	private String pointName;
	
	public Point() {}

	public Point(String pointName) {
		super();
		this.pointName = pointName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<RoutePoint> getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(List<RoutePoint> routePoints) {
		this.routePoints = routePoints;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	@Override
	public String toString() {
		return "Point [id=" + id + ", pointName=" + pointName + "]";
	}
}
