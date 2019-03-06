package app.dto;

import app.entities.Route;

public class RouteDTO {
	
	private Route route;

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "RouteDTO [route=" + route + "]";
	}
}
