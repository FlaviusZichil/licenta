package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.UserTrip;

@Repository
public interface UserTripRepository extends CrudRepository<UserTrip, Integer>{

}
