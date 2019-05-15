package app.dto;

import java.util.List;
import app.entities.UserMedal;

public class MedalDTO {

	private Integer medalId;
    private PeakDTO peakDTO;
    private boolean owned;
    private List<UserMedal> userMedals;
    
    public MedalDTO() {}
    
	public MedalDTO(Integer medal_id, PeakDTO peakDTO) {
		super();
		this.medalId = medal_id;
		this.peakDTO = peakDTO;
		this.owned = false;
	}
	
	public Integer getMedal_id() {
		return medalId;
	}
	public Integer getMedalId() {
		return medalId;
	}

	public void setMedalId(Integer medalId) {
		this.medalId = medalId;
	}

	public boolean isOwned() {
		return owned;
	}

	public void setOwned(boolean owned) {
		this.owned = owned;
	}

	public void setMedal_id(Integer medal_id) {
		this.medalId = medal_id;
	}
	public PeakDTO getPeakDTO() {
		return peakDTO;
	}
	public void setPeakDTO(PeakDTO peakDTO) {
		this.peakDTO = peakDTO;
	}
	public List<UserMedal> getUserMedals() {
		return userMedals;
	}
	public void setUserMedals(List<UserMedal> userMedals) {
		this.userMedals = userMedals;
	}

	@Override
	public String toString() {
		return "MedalDTO [medalId=" + medalId + ", peakDTO=" + peakDTO + ", owned=" + owned + "]";
	}
}
