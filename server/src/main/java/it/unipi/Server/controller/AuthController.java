package it.unipi.Server.controller;

import it.unipi.Server.model.User;
import it.unipi.Server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * API per gestione autenticazione.
 * @author lucamaffioli
 */
@Controller
@RequestMapping(path="/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping(path="/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        try {
            String token = authService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            );
            if (token != null) {
                return new ResponseEntity(token, HttpStatus.OK); 
            }
            throw new RuntimeException("LOGIN_FAILED");
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
    
    @PostMapping(path="/logout")
    @ResponseBody
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null) {
            authService.logout(token);
        }
        return new ResponseEntity("LOGGED_OUT", HttpStatus.OK);
    }
    
    @PostMapping(path="/signup")
    @ResponseBody
    public ResponseEntity signup(@RequestBody User signinRequest) {  
        try {
            String result = authService.register(signinRequest.getUsername(), signinRequest.getPassword());
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
