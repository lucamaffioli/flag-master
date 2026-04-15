package it.unipi.Server.service;

import it.unipi.Server.dto.AddScoreRequest;
import it.unipi.Server.dto.ListStatsDTO;
import it.unipi.Server.model.Score;
import it.unipi.Server.model.User;
import it.unipi.Server.model.UserList;
import it.unipi.Server.repository.ScoreRepository;
import it.unipi.Server.repository.UserListRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Gestione punteggi.
 * @author lucamaffioli
 */
@Service
public class ScoreService {
    
    private static final Logger logger = LoggerFactory.getLogger(ScoreService.class);
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    @Autowired
    private UserListRepository userListRepository;
    
    /**
     * Aggiunge il punteggio di una partita.
     * @param user Utente che ha effettuato la partita.
     * @param request Richiesta da parte dell'utente con le informazioni da salvare.
     */
    public void addScore(User user, AddScoreRequest request) {
        
        Optional<UserList> listOpt = userListRepository.findById(request.getListId());
        
        if (listOpt.isEmpty()) {
            logger.warn("Id lista non valido");
            throw new RuntimeException("INVALID_LIST");
        }
        
        UserList list = listOpt.get();
        
        if (!list.getOwner().equals(user)) {
            logger.warn("La lista non è di proprietà dell'utente user");
            throw new RuntimeException("LIST_NOT_AVAILABLE");
        }
        
        // Creazione di un punteggio per la lista specificata.
        Score score = new Score(user, list, request.getPoints(), request.getTotalQuestions());
        
        scoreRepository.save(score);
    }
    
    /**
     * Funzione per recuperare, ordinare e restituire informazione relative alle
     * partite giocate sulle diverse liste.
     * @param user utente.
     * @return Informazioni suddivise per lista.
     */
    public List<ListStatsDTO> stats(User user) {
        
        List<UserList> lists = userListRepository.findByOwner(user);
        
        List<Score> scores = scoreRepository.findByUser(user);
        
        List<ListStatsDTO> stats = new ArrayList<>();
        
        // Per ogni lista di user.
        for (UserList l: lists) {
            
            // List delle partite relative alla lista l.
            List<Score> scoresThisList = scores.stream()
                    .filter(s -> s.getList().getId().equals(l.getId()))
                    .collect(Collectors.toList());
            
            // Numero di partite giocate sulla lista.
            int totGames = scoresThisList.size();
            // Numero di domande in totale.
            int totQuestions = scoresThisList.stream().mapToInt(Score::getQuestions).sum();
            // Numero di risposte corrette in totale.
            int totPoints = scoresThisList.stream().mapToInt(Score::getPoints).sum();
            
            stats.add(new ListStatsDTO(l.getId(), l.getName(), totGames, totQuestions, totPoints));
            
        }
        return stats;
    }
}
