package app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.entities.PromoCode;

@Repository
public interface PromoCodeRepository extends CrudRepository<PromoCode, Integer>{

}
