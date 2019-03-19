package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.Guide;

@Repository
public interface GuideRepository extends CrudRepository<Guide, Integer> {

}
