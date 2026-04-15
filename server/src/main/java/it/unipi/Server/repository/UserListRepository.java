package it.unipi.Server.repository;

import it.unipi.Server.model.User;
import it.unipi.Server.model.UserList;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lucamaffioli
 */
@Repository
public interface UserListRepository extends CrudRepository<UserList, Integer> {
    // Trova liste di un utente specificato.
    List<UserList> findByOwner(User Owner);
}
