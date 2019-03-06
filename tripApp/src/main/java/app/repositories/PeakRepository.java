package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Peak;

@Repository
public interface PeakRepository extends CrudRepository<Peak, Integer>{

}
