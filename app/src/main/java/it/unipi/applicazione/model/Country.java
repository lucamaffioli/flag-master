package it.unipi.applicazione.model;

import java.util.Objects;

/**
 * Classe che rappresenta un paese.
 * @author lucamaffioli
 */

public class Country {
    
    private Integer id;
    
    private String name;
    
    // Continente di appartenenza.
    private String region;
    
    private String capital;
    
    private String flagUrl;
    
    public Country() {}

    public Country(String name, String region, String capital, String flagUrl) {
        this.name = name;
        this.region = region;
        this.capital = capital;
        this.flagUrl = flagUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }
    
    @Override
    public String toString() {
        return name; 
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country c = (Country)o;
        return Objects.equals(this.id, c.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
