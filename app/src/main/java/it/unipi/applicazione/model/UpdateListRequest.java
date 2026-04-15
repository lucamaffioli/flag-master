package it.unipi.applicazione.model;

import java.util.List;

/**
 * Richiesta aggiornamento lista.
 * @author lucamaffioli
 */
public class UpdateListRequest {
    
    private Integer id;
    
    private String name;
    
    private List<Integer> countriesIds;

    public UpdateListRequest() {}

    public List<Integer> getCountriesIds() {
        return countriesIds;
    }

    public void setCountriesIds(List<Integer> countriesIds) {
        this.countriesIds = countriesIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
      
}
