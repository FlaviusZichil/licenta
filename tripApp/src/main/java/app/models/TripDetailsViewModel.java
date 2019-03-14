package app.models;

import app.dto.TripDTO;

public class TripDetailsViewModel {
	
	private TripDTO tripDTO;

	public TripDTO getTripDTO() {
		return tripDTO;
	}

	public void setTripDTO(TripDTO tripDTO) {
		this.tripDTO = tripDTO;
	}

	@Override
	public String toString() {
		return "TripDetailsViewModel [tripDTO=" + tripDTO + "]";
	}
}
