package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.RoutePoint;

@Repository
public interface RoutePointRepository extends CrudRepository<RoutePoint, Integer> {

}
