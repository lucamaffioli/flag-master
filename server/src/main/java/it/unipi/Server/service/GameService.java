package it.unipi.Server.service;

import it.unipi.Server.dto.QuestionDTO;
import it.unipi.Server.model.Country;
import it.unipi.Server.model.UserList;
import it.unipi.Server.repository.UserListRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Funzioni per gestione partita.
 * @author lucamaffioli
 */
@Service
public class GameService {
 
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    
    @Autowired
    private UserListRepository userListRepository;
    
    // Numero di domande per partita.
    private static final int DEFAULT_NUM_QUESTIONS = 5;
    // Numero di opzioni per domanda.
    private static final int OPTIONS_PER_QUESTION = 3;

    /**
     * Creazione nuova partita.
     * @param listID id della lista sulla quale giocare.
     * @return Lista di domande.
     */
    public List<QuestionDTO> newGame(Integer listID) {
        
        Optional<UserList> listOpt = userListRepository.findById(listID);
        if (listOpt.isEmpty()) {
            logger.warn("Lista {} non trovata", listID);
            throw new RuntimeException("LIST_NOT_FOUND");
        }
        
        UserList list = listOpt.get();
        
        List<Country> countries = list.getCountries();
        
        if (countries.size() < OPTIONS_PER_QUESTION) {
            throw new RuntimeException("NOT_ENOUGHT_COUNTRIES_IN_THE_LIST");
        }
        
        int numQuestions = Integer.min(countries.size(), DEFAULT_NUM_QUESTIONS); 
        
        // Lista di paesi di appoggio.
        List<Country> shuffledList = new ArrayList<>(countries);
        // Ordinamento casuale.
        Collections.shuffle(shuffledList);
        
        // Scelta paesi per le domande.
        List<Country> targetCountries = shuffledList.subList(0, numQuestions);
        
        List<QuestionDTO> gameQuestions = new ArrayList<>();
        
        // Per ogni paese selezionato creo una domanda.
        for (Country target : targetCountries) {
            gameQuestions.add(createQuestion(target, countries));
        }
        
        return gameQuestions;
    }
    
    /**
     * Crea una domanda.
     * @param target risposta corretta.
     * @param countries tutti i paesi della lista (alternative compreso target).
     * @return Una domanda.
     */
    private QuestionDTO createQuestion(Country target, List<Country> countries) {
        
        // Selezione di tutte le possibili risposte sbagliate.
        List<Country> wrongCountries = new ArrayList<>(countries);
        wrongCountries.remove(target);
        
        // Ordinamento casuale.
        Collections.shuffle(wrongCountries);
        
        List<String> options = new ArrayList<>();
        
        // Compongo la lista delle opzioni sbagliate.
        for (int i = 0; i < OPTIONS_PER_QUESTION - 1; i++) {
            options.add(wrongCountries.get(i).getName());
        }
        
        // Aggiungo alle opzioni sbagliate quella corretta.
        options.add(target.getName());
        // Ordinamento casuale.
        Collections.shuffle(options);
        
        return new QuestionDTO(target.getFlagUrl(), target.getName(), options);
    }    
}
