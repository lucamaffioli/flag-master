package it.unipi.Server.service;

import it.unipi.Server.model.Country;
import it.unipi.Server.repository.CountryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Funzioni per recupero dati paesi.
 * @author lucamaffioli
 */
@Service
public class CountryService {
    
    @Autowired
    private CountryRepository countryRepository;
    
    public List<String> getRegions() {
        return countryRepository.findAllRegions();
    }
    
    public List<Country> getRegionCountries(String region) {
        return countryRepository.findAllByRegion(region);
    }
    
}
