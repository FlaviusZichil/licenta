package app.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import app.dto.PointDTO;
import app.dto.RoutePointDTO;
import app.entities.Guide;
import app.entities.Route;
import app.entities.RoutePoint;
import app.entities.Trip;

public class TripUtils {
	public static List<RoutePointDTO> getRoutePointsDTOForTrip(Trip trip) {
		List<RoutePointDTO> routePointsDTO = new ArrayList<>();
		Route route = trip.getRoute();
		List<RoutePoint> routePoints = route.getRoutePoints();
		
		for (RoutePoint routePoint : routePoints) {
			PointDTO pointDTO = new PointDTO(routePoint.getPoint().getId(), routePoint.getPoint().getPointName());
			RoutePointDTO routePointDTO = new RoutePointDTO(routePoint.getId(), routePoint.getOrder(), pointDTO);
			routePointsDTO.add(routePointDTO);
		}
		return sortPointsByOrder(routePointsDTO);
	}

	private static List<RoutePointDTO> sortPointsByOrder(List<RoutePointDTO> routePointsDTO) {
		Collections.sort(routePointsDTO, new Comparator<RoutePointDTO>() {
			@Override
			public int compare(RoutePointDTO firstRoutePointDTO, RoutePointDTO secondRoutePointDTO) {
				if (Integer.parseInt(firstRoutePointDTO.getOrder()) > Integer
						.parseInt(secondRoutePointDTO.getOrder())) {
					return 1;
				}

				if (Integer.parseInt(firstRoutePointDTO.getOrder()) < Integer
						.parseInt(secondRoutePointDTO.getOrder())) {
					return -1;
				}
				return 0;
			}
		});

		return routePointsDTO;
	}

	public static List<PointDTO> sortPointsByMountain(List<PointDTO> pointsDTO) {
		Collections.sort(pointsDTO, new Comparator<PointDTO>() {
			@Override
			public int compare(PointDTO firstPointDTO, PointDTO secondPointDTO) {
				if (firstPointDTO.getMountainDTO().getName().compareTo(secondPointDTO.getMountainDTO().getName()) > 0) {
					return 1;
				}
				if (firstPointDTO.getMountainDTO().getName().compareTo(secondPointDTO.getMountainDTO().getName()) < 0) {
					return -1;
				}
				return 0;
			}
		});

		return pointsDTO;
	}

	public static Integer getNumberOfFinishedTripsWithActiveStatusForGuide(Guide guide) {
		Integer number = 0;
		for (Trip trip : guide.getTrips()) {
			LocalDate endDate = LocalDate.parse(trip.getEndDate());
			if (endDate.isEqual(LocalDate.now())
					|| endDate.isBefore(LocalDate.now()) && trip.getStatus().equals("Active")) {
				number++;
			}
		}
		System.out.println(number);
		return number;
	}
}
