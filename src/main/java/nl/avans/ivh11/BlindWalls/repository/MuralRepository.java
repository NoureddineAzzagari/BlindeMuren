package nl.avans.ivh11.BlindWalls.repository;

import nl.avans.ivh11.BlindWalls.domain.mural.Mural;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuralRepository extends CrudRepository<Mural, Long> {

}
