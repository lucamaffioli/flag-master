package it.unipi.Server.dto;

import java.util.List;

/**
 * Richiesta di aggiornamento di una lista.
 * @author lucamaffioli
 */
public class UpdateListRequest {
    
    // Id lista da aggiornare.
    private Integer id;
    // Nome lista.
    private String name;
    // Lista di id dei paesi inclusi nella lista.
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
