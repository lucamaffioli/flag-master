package it.unipi.applicazione.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista di paesi.
 * @author lucamaffioli
 */
public class UserListDTO {
    
    private Integer id;
    private String name;
    private List<Country> countries;

    public UserListDTO(Integer id, String name, List<Country> countries) {
        this.id = id;
        this.name = name;
        this.countries = countries;
    }

    public UserListDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.countries = new ArrayList<>();
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

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
    
    // Necessario per "rappresentare" la struttura.
    @Override
    public String toString() {
        return name;
    }
    
}
