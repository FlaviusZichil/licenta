package app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "route_point")
public class RoutePoint {

	@Id
	@Column(name = "route_point_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn
	private Route route;

	@ManyToOne
	@JoinColumn
	private Point point;

	@Column(name = "orderOfPoint")
	private String orderOfPoint;
	
	public RoutePoint() {}

	public RoutePoint(Route route, Point point, String orderOfPoint) {
		super();
		this.route = route;
		this.point = point;
		this.orderOfPoint = orderOfPoint;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getOrder() {
		return orderOfPoint;
	}

	public void setOrder(String order) {
		this.orderOfPoint = order;
	}

	@Override
	public String toString() {
		return "RoutePoint [id=" + id + ", route=" + route + ", point=" + point + ", orderOfPoint=" + orderOfPoint
				+ "]";
	}
}
