package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Register;

@Repository
public interface RegisterRepository extends CrudRepository<Register, Integer> {

}
