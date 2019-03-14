package app.dto;

import app.entities.Peak;

public class PeakDTO {

	private Peak peak;
	private MountainDTO mountainDTO;
	
	public Peak getPeak() {
		return peak;
	}
	public void setPeak(Peak peak) {
		this.peak = peak;
	}
	public MountainDTO getMountainDTO() {
		return mountainDTO;
	}
	public void setMountainDTO(MountainDTO mountainDTO) {
		this.mountainDTO = mountainDTO;
	}
	
	@Override
	public String toString() {
		return "PeakDTO [peak=" + peak + ", mountainDTO=" + mountainDTO + "]";
	}
}
