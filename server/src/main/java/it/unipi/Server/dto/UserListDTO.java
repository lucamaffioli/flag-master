
package it.unipi.Server.dto;

import it.unipi.Server.model.Country;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO trasferimento lista.
 * @author lucamaffioli
 */
public class UserListDTO {
    // Id lista.
    private Integer id;
    // Nome lista.
    private String name;
    // Lista dei paesi inclusi nella lista.
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
    
}
