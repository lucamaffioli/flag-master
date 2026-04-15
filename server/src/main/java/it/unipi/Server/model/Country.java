package it.unipi.Server.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 
 * @author lucamaffioli
 */
@Entity
@Table(name = "countries")
public class Country implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "common_name")
    private String name;
    
    @Column(name = "region")
    private String region;
    
    @Column(name = "capital")
    private String capital;
    
    @Column(name = "flag_url", length = 500)
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

    public void setId(Integer id) {
        this.id = id;
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
