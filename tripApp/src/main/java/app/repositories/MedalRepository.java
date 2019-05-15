package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Medal;

@Repository
public interface MedalRepository extends CrudRepository<Medal, Integer> {

}
