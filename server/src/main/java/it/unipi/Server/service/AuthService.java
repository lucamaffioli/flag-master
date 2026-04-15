package it.unipi.Server.service;

import it.unipi.Server.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.unipi.Server.model.User;
import it.unipi.Server.model.UserList;
import it.unipi.Server.repository.CountryRepository;
import it.unipi.Server.repository.UserListRepository;
import it.unipi.Server.repository.UserRepository;
import it.unipi.Server.utils.Crypto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestione autenticazione utente.
 * @author lucamaffioli
 */
@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CountryRepository countryRepository; 
    
    @Autowired
    private UserListRepository userListRepository;
    
    // Un token assegnato ad un utente scade dopo 30 minuti di inattività.
    private static final long DURATION_TOKEN = 30;
    
    // Necessario il concetto di sessione per implementare la scadenza del token.
    private class Session {
        User user;
        LocalDateTime expireTime;
        
        public Session(User user) {
            this.user = user;
            this.expireTime = LocalDateTime.now().plusMinutes(DURATION_TOKEN);
        }
    }
    
    /**
     * Rinnovo scadenza token.
     * Dopo DURATION_TOKEN minuti di inattività il token viene invalidato.
     * @param token 
     */
    public void renewExpiration(String token) {
        Session current = activeSessions.get(token);
        current.expireTime = LocalDateTime.now().plusMinutes(DURATION_TOKEN);
    }
    
    /*
    Mappa che associa ad ogni token una sessione (utente + scadenza).
    Necessaria per gestire connessioni da più utenti.
    */
    private final Map<String, Session> activeSessions = new HashMap<>();
    
    
    /**
     * Controlla se l'utente è già presente altrimenti crea un nuovo utente e 
     * lo aggiunge al db.
     * @param username 
     * @param password
     * @return "USER_ALREADY_EXISTS" o "USER_ADD_CORRECTLY".
     */
    @Transactional
    public String register(String username, String password) {
        
        if (countryRepository.count() == 0) {
            logger.warn("Inizializzazione DB non ancora avvenuta", username);
            throw new RuntimeException("INITIALIZE_DB_FIRST");
        }
        
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            logger.info("Username '{}' già in uso. Registrazione fallita", username);
            throw new RuntimeException("USER_ALREADY_EXISTS");
        }
        
        
        
        String hashpasswd = Crypto.sha256(password);
        User newUser = new User(username, hashpasswd);
        User savedUser = userRepository.save(newUser);
        
        // creazione di tre liste di default.
        createDefaultList(savedUser, "Start Europe", "Europe");
        createDefaultList(savedUser, "Start Asia", "Asia");
        createDefaultList(savedUser, "Start Americas", "Americas");
        
        logger.info("Utente '{}' registrato con successo", username);
        return "USER_ADD_CORRECTLY";
    } 
    
    /**
    * Ricerca username e verifica password, inoltre viene generato un token di sessione
    * legato all'utente.
    * @param username utente.
    * @param password password in chiaro passata dall'utente.
    * @return token o null in alternativa
    */
    public String authenticate(String username, String password) {
        
        if (countryRepository.count() == 0) {
            logger.warn("Inizializzazione DB non ancora avvenuta", username);
            throw new RuntimeException("INITIALIZE_DB_FIRST");
        }
        
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            User dbUser = user.get();
            if (dbUser.getPassword().equals(Crypto.sha256(password))) {
                logger.info("Username '{}' password corretta", username);
                // Genereazione token univoco e casuale.
                String token = UUID.randomUUID().toString();
                activeSessions.put(token, new Session(dbUser));
                return token;
            } else {
                logger.info("Username '{}' password non corretta", username);
                return null;
            }
        }
        logger.info("Username '{}' non trovato", username);
        return null;
    }
    
    /**
     * Rimozione token sessione.
     * @param token 
     */
    public void logout(String token) {
        activeSessions.remove(token);
    }
    
    /**
     * Restituisce l'utente collegato al token. 
     * Controlla la presenza del token e la validità.
     * @param token
     * @return user associato
     */
    public User getUserByToken(String token) {
        if (!activeSessions.containsKey(token)) {
            logger.warn("Token non trovato.");
            return null;
        }
        Session session = activeSessions.get(token);
        if (LocalDateTime.now().isAfter(session.expireTime)) {
            logger.info("Token scaduto per utente: {}", session.user.getUsername());
            activeSessions.remove(token);
            return null;
        }
        return session.user;
    }
    
    
    /**
     * Funzione per la creazione di liste di default alla registrazione di un utente.
     * @param user utente.
     * @param listName nome lista.
     * @param region continente dei paesi.
     */
    private void createDefaultList(User user, String listName, String region) {
        // Recupera i paesi dal DB
        List<Country> countries = countryRepository.findAllByRegion(region);
        
        // Pochi paesi.
        if (countries.size() > 15) {
            countries = countries.subList(0, 15);
        }
        
        UserList defaultList = new UserList();
        defaultList.setName(listName);
        defaultList.setOwner(user);
        defaultList.setCountries(countries);
        
        userListRepository.save(defaultList);
    }
    
}
