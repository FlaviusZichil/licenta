package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Tombola;

@Repository
public interface TombolaRepository extends CrudRepository<Tombola, Integer>{

}
