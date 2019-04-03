package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Point;

@Repository
public interface PointRepository extends CrudRepository<Point, Integer>{

}
