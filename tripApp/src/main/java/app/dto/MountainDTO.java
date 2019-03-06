package app.dto;

import app.entities.Mountain;

public class MountainDTO {
	
	private Mountain mountain;

	public Mountain getMountain() {
		return mountain;
	}

	public void setMountain(Mountain mountain) {
		this.mountain = mountain;
	}

	@Override
	public String toString() {
		return "MountainDTO [mountain=" + mountain + "]";
	}
}
