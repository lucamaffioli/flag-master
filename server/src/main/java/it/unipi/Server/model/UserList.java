package it.unipi.Server.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucamaffioli
 */
@Entity
@Table(name = "user_lists")
public class UserList implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "list_name")
    private String name;
    
    // Molte liste possono appartenere ad un solo utente.
    // @JoinColumn(name = "owner_username") serve per creare nella tabella la 
    // colonna owner_username come chiave esterna.
    @ManyToOne
    @JoinColumn(name = "owner_username")
    private User owner;
    
    // Una lista può contenere molti paesi, un paese può stare in molte liste.
    // FetchType.EAGER per caricare i paesi della lista subito e non solo
    // il nome della lista.
    @ManyToMany(fetch = FetchType.EAGER)
    // Crea la tabella di unione JoinTable.
    @JoinTable(
            name = "list_contents",
            joinColumns = @JoinColumn(name = "list_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> countries = new ArrayList<>();
    
    // Collegamento tra una lista e gli score relativi ad essa.
    // In qeusto modo quando viene eliminata una lista vengono 
    // eliminati anche gli score nella tabella degli score.
    @OneToMany(mappedBy = "list", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();
    
    
    public UserList() {}

    public void setId(Integer id) {
        this.id = id;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
    
}
