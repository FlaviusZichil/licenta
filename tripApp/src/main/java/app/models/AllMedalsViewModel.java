package app.models;

import java.util.List;

import app.dto.MedalDTO;

public class AllMedalsViewModel {

	private List<MedalDTO> medalsDTO;

	public List<MedalDTO> getMedalsDTO() {
		return medalsDTO;
	}

	public void setMedalsDTO(List<MedalDTO> medalsDTO) {
		this.medalsDTO = medalsDTO;
	}

	@Override
	public String toString() {
		return "AllMedalsViewModel [medalsDTO=" + medalsDTO + "]";
	}
}
