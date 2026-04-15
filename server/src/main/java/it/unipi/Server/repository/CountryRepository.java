package it.unipi.Server.repository;

import it.unipi.Server.model.Country;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lucamaffioli
 */
@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    // Funzione (Query) per trovare tutte le regioni.
    @Query ("SELECT DISTINCT c.region FROM Country c WHERE c.region IS NOT NULL ORDER BY c.region ASC")
    List<String> findAllRegions();
    
    // Funzione (Query) per trovare tutti i paesi in una data regione.
    List<Country> findAllByRegion(String region);
}
