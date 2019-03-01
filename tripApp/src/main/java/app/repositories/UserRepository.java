package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import app.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{

}
