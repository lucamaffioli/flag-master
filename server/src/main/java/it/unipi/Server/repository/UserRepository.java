package it.unipi.Server.repository;

import it.unipi.Server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lucamaffioli
 */
@Repository
public interface UserRepository extends CrudRepository<User, String>{}
