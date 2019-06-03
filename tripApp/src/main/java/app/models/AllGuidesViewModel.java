package app.models;

import java.util.List;

import app.dto.GuideDTO;

public class AllGuidesViewModel {

	private List<GuideDTO> guidesDTO;

	public List<GuideDTO> getGuidesDTO() {
		return guidesDTO;
	}

	public void setGuidesDTO(List<GuideDTO> guidesDTO) {
		this.guidesDTO = guidesDTO;
	}
}
