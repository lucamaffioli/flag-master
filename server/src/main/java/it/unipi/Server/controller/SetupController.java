package it.unipi.Server.controller;

import it.unipi.Server.service.SetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * API per inizializzazione DB.
 * @author lucamaffioli
 */

@Controller
@RequestMapping(path="/api/setup")
public class SetupController {
    
    @Autowired
    private SetupService setupService;
    
    @PostMapping(path="/inizializza")
    @ResponseBody
    public ResponseEntity inizializza() {
        try {
            setupService.inizializzaDB();
            return new ResponseEntity("INIZIALIZATION_COMPLEATED", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
}
