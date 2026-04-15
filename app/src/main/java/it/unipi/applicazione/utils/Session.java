package it.unipi.applicazione.utils;

import it.unipi.applicazione.model.GameResult;
import it.unipi.applicazione.model.User;
import it.unipi.applicazione.model.UserListDTO;
import java.util.List;

/**
 * Classe per mantenere informazioni sulla sessione corrente.
 * Inoltre vengono salvati oggetti necessari durante il passaggio tra più schermate.
 * @author lucamaffioli
 */
public class Session {
    
    // Unica istanza della classe (Singleton)
    private static Session instance;
    
    // Dati da memorizzare in modo permanente.
    private String token;
    private User loggedUser;
    
    // Campo necessario per mentenere informazioni durante il passaggio tra interfaccie.
    private UserListDTO selectedList;
    // Informazioni sui risultati delle domande durante il corso di una partita.
    private List<GameResult> lastGameResults;
    
    private Session() {}
    
    /**
     * Metodo pubblico per ottenere l'istanza della classe.
     * Se null viene creata tramite costruttore privato.
     * @return istanza classe.
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    // Salvataggio dati (dopo il login)
    public void setSession(String token, User user) {
        this.token = token;
        this.loggedUser = user;
    }
    
    // Rimozione dati (dopo il logout)
    public void clearSession() {
        this.token = null;
        this.loggedUser = null;
    }
    
    // Getter
    public String getToken() {
        return token;
    }
    
    public User getLoggedUser() {
        return loggedUser;
    }

    public UserListDTO getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(UserListDTO selectedList) {
        this.selectedList = selectedList;
    }

    public List<GameResult> getLastGameResults() {
        return lastGameResults;
    }

    public void setLastGameResults(List<GameResult> lastGameResults) {
        this.lastGameResults = lastGameResults;
    }
    
}
