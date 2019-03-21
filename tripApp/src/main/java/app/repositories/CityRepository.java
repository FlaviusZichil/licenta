package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.City;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

}
