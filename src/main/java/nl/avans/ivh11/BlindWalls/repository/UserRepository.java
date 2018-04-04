package nl.avans.ivh11.BlindWalls.repository;

import nl.avans.ivh11.BlindWalls.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
