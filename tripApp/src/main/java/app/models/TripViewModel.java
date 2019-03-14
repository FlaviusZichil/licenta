package app.models;

import java.util.List;

import app.dto.TripDTO;

public class TripViewModel {
	
	private List<TripDTO> tripsDTO;

	public List<TripDTO> getTripsDTO() {
		return tripsDTO;
	}

	public void setTripsDTO(List<TripDTO> tripsDTO) {
		this.tripsDTO = tripsDTO;
	}

	@Override
	public String toString() {
		return "TripViewModel [tripsDTO=" + tripsDTO + "]";
	}
}
