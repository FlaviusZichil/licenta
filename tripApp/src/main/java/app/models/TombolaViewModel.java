package app.models;

import java.util.List;

import app.dto.TombolaDTO;

public class TombolaViewModel {
	
	private List<TombolaDTO> winners;

	public List<TombolaDTO> getWinners() {
		return winners;
	}

	public void setWinners(List<TombolaDTO> registrationsToTombola) {
		this.winners = registrationsToTombola;
	}
}
