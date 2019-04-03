package app.models;

import java.util.List;

import app.dto.CityDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.PointDTO;

public class AddTripViewModel {

	private List<PeakDTO> peaksDTO;
	private List<MountainDTO> mountainsDTO;
	private List<CityDTO> citiesDTO;
	private List<PointDTO> pointsDTO;
	
	public List<PeakDTO> getPeaksDTO() {
		return peaksDTO;
	}
	public void setPeaksDTO(List<PeakDTO> peaksDTO) {
		this.peaksDTO = peaksDTO;
	}
	public List<MountainDTO> getMountainsDTO() {
		return mountainsDTO;
	}
	public void setMountainsDTO(List<MountainDTO> mountainsDTO) {
		this.mountainsDTO = mountainsDTO;
	}
	public List<CityDTO> getCitiesDTO() {
		return citiesDTO;
	}
	public void setCitiesDTO(List<CityDTO> citiesDTO) {
		this.citiesDTO = citiesDTO;
	}
	public List<PointDTO> getPointsDTO() {
		return pointsDTO;
	}
	public void setPointsDTO(List<PointDTO> pointsDTO) {
		this.pointsDTO = pointsDTO;
	}
}
