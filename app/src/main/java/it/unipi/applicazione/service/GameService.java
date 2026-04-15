package it.unipi.applicazione.service;

import com.google.gson.reflect.TypeToken;
import it.unipi.applicazione.model.AddScoreRequest;
import it.unipi.applicazione.model.QuestionDTO;
import java.util.List;

/**
 * Servizi gestione partita.
 * @author lucamaffioli
 */
public class GameService extends APIclient{
    
    public List<QuestionDTO> getQuestions(int listId) throws Exception {
        return get("/game/new/" + listId, new TypeToken<List<QuestionDTO>>(){}.getType()); 
    }
    
    public String addScore(AddScoreRequest addScoreRequest) throws Exception {
        return post("/score/add", addScoreRequest, String.class);
    }
    
}
