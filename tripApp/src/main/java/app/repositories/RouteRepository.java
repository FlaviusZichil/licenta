package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Route;

@Repository
public interface RouteRepository extends CrudRepository<Route, Integer>{

}
