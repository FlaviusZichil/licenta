package app.models;

import java.util.List;

import app.dto.TombolaResultsDTO;

public class TombolaResultsViewModel {

	private List<TombolaResultsDTO> tombolaResultsDTO;

	public List<TombolaResultsDTO> getTombolaResultsDTO() {
		return tombolaResultsDTO;
	}

	public void setTombolaResultsDTO(List<TombolaResultsDTO> tombolaResultsDTO) {
		this.tombolaResultsDTO = tombolaResultsDTO;
	}

	@Override
	public String toString() {
		return "TombolaResultsViewModel [tombolaResultsDTO=" + tombolaResultsDTO + "]";
	}
}
