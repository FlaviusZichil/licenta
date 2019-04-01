package app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.dto.CityDTO;
import app.dto.GuideDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.PointDTO;
import app.dto.RouteDTO;
import app.dto.RoutePointDTO;
import app.dto.TripDTO;
import app.entities.Route;
import app.entities.RoutePoint;
import app.entities.Trip;

public class TripUtils {

	public static List<RoutePointDTO> getRoutePointsDTOForTrip(Trip trip){
		List<RoutePointDTO> routePointsDTO = new ArrayList<>();
		
		Route route = trip.getRoute();
		List<RoutePoint> routePoints = route.getRoutePoints();
		
		for(RoutePoint routePoint : routePoints) {
			PointDTO pointDTO = new PointDTO(routePoint.getPoint().getId(), routePoint.getPoint().getPointName());
			RoutePointDTO routePointDTO = new RoutePointDTO(routePoint.getId(), routePoint.getOrder(), pointDTO);
			routePointsDTO.add(routePointDTO);
		}	
		return sortPointsByOrder(routePointsDTO);
	}
	
	public static <T> List<T> convertFromIterableToList(Iterable<T> iterable){
		List<T> list = new ArrayList<T>();
		
		for(T item : iterable) {
			list.add(item);
		}
		return list;
	}
	
	public static TripDTO convertFromTripToTripDTO(Trip trip) {
		CityDTO cityDTO = new CityDTO(trip.getPeak().getCity().getName(), trip.getPeak().getCity().getLatitude(), trip.getPeak().getCity().getLongitude());
		MountainDTO mountainDTO = new MountainDTO(trip.getPeak().getMountain().getMountainName());
		PeakDTO peakDTO = new PeakDTO(trip.getPeak().getId(), trip.getPeak().getPeakName(), trip.getPeak().getAltitude(), cityDTO, 
									  mountainDTO, trip.getPeak().getTrips());
		GuideDTO guideDTO = new GuideDTO(trip.getGuide().getId(), trip.getGuide().getUser(), trip.getGuide().getYearsOfExperience(), trip.getGuide().getPhoneNumber());
		RouteDTO routeDTO = new RouteDTO(trip.getRoute().getId(), trip.getRoute().getDifficulty(), getRoutePointsDTOForTrip(trip));
		
		TripDTO tripDTO = new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
									  trip.getUsers(), routeDTO, peakDTO, guideDTO);
		return tripDTO;
	}
	
	private static List<RoutePointDTO> sortPointsByOrder(List<RoutePointDTO> routePointsDTO){
		Collections.sort(routePointsDTO, new Comparator<RoutePointDTO>(){
			   @Override
			   public int compare(RoutePointDTO firstRoutePointDTO, RoutePointDTO secondRoutePointDTO) {				   
				   if(Integer.parseInt(firstRoutePointDTO.getOrder()) > Integer.parseInt(secondRoutePointDTO.getOrder())) {
					   return 1;
				   }
				   
				   if(Integer.parseInt(firstRoutePointDTO.getOrder()) < Integer.parseInt(secondRoutePointDTO.getOrder())) {
					   return -1;
				   }
				   return 0;
			     }
			 });
		
		return routePointsDTO;
	}
}
