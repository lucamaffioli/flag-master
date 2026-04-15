package it.unipi.applicazione.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Utente.
 * @author lucamaffioli
 */
public class User implements Serializable {
    
    private String username;
    
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
        User c = (User)o;
        return Objects.equals(this.username, c.username);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
}
