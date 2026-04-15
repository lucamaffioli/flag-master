package it.unipi.Server.controller;

import it.unipi.Server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API gestione partita.
 * @author lucamaffioli
 */
@RestController
@RequestMapping(path ="/api/game")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @GetMapping(path="/new/{listId}")
    @ResponseBody
    public ResponseEntity newGame(@PathVariable Integer listId) { 
        try {
            return new ResponseEntity(gameService.newGame(listId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }
}
