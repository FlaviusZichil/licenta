package app.dto;

import app.entities.Trip;

public class TripDTO {

	private Trip trip;
	
	public TripDTO(Trip trip) {
		this.trip = trip;
	}
	
	public Trip getTrip() {
		return trip;
	}
	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	@Override
	public String toString() {
		return "TripDTO [trip=" + trip + "]";
	}
}
