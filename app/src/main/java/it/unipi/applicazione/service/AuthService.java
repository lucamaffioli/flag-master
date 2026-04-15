package it.unipi.applicazione.service;

import it.unipi.applicazione.model.User;
import it.unipi.applicazione.utils.Session;

/**
 * Servizi per autenticazione.
 * @author lucamaffioli
 */
public class AuthService extends APIclient {
    
    public void login(String username, String password) throws Exception {  
        User user = new User(username, password);
        String token = post("/auth/login", user, String.class);
        // Salvataggio sessione corrente.
        Session.getInstance().setSession(token, user);
    }
    
    public String logout() throws Exception {    
        return post("/auth/logout", null, String.class);
    }
    
    public String signup(String username, String password) throws Exception {
        User user = new User(username, password);
        return post("/auth/signup", user, String.class);
    }
}
