package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Mountain;

@Repository
public interface MountainRepository extends CrudRepository<Mountain, Integer>{

}
