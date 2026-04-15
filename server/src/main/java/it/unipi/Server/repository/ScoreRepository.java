package it.unipi.Server.repository;

import it.unipi.Server.model.Score;
import it.unipi.Server.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lucamaffioli
 */
@Repository
public interface ScoreRepository extends CrudRepository<Score, Integer> {
    // Trova tutti gli score di un utente.
    List<Score> findByUser(User user);
}
 