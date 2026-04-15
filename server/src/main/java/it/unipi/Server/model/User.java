package it.unipi.Server.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author lucamaffioli
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
    
    @Id
    @Column(name = "username", length = 50)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User u = (User)o;
        return Objects.equals(this.username, u.username);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
