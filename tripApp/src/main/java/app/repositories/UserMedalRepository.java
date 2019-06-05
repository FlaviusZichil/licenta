package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.UserMedal;

@Repository
public interface UserMedalRepository extends CrudRepository<UserMedal, Integer>{

}
