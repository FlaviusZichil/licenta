package app.repositories;

import org.springframework.data.repository.CrudRepository;

import app.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer>{

}
