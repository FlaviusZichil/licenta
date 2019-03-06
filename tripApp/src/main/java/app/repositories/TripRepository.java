package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Trip;

@Repository
public interface TripRepository extends CrudRepository<Trip, Integer>{

}
