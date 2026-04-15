package it.unipi.Server.controller;

import it.unipi.Server.dto.AddScoreRequest;
import it.unipi.Server.model.User;
import it.unipi.Server.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API gestione punteggi partite.
 * @author lucamaffioli
 */
@RestController
@RequestMapping("/api/score")
public class ScoreController {
    
    @Autowired 
    private ScoreService scoreService;
    
    // Aggiunge risultati di una partita.
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity addScore(@RequestAttribute("loggedUser") User user, @RequestBody AddScoreRequest request) {     
        try {
            scoreService.addScore(user, request);
            return new ResponseEntity("SCORE_SAVED", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Recupera statistiche delle partite giocate dall'utente.
    @GetMapping(path="/stats")
    @ResponseBody
    public ResponseEntity stats(@RequestAttribute("loggedUser") User user) {
        try {
            return new ResponseEntity(scoreService.stats(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    } 
}
