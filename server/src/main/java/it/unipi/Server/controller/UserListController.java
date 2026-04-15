package it.unipi.Server.controller;

import it.unipi.Server.dto.UpdateListRequest;
import it.unipi.Server.dto.UserListDTO;
import it.unipi.Server.model.User;
import it.unipi.Server.service.UserListService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API per gestione liste paesi utente.
 * @author lucamaffioli
 */
@RestController
@RequestMapping("/api/list")
public class UserListController {
    
    @Autowired UserListService userListService;
    
    @GetMapping("/my-lists")
    @ResponseBody
    public ResponseEntity<List<UserListDTO>> myLists(@RequestAttribute("loggedUser") User user) {
        try {
            return new ResponseEntity(userListService.getListByUsers(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
    
    @PostMapping("/delete/{id}") 
    @ResponseBody
    public ResponseEntity delete(@RequestAttribute("loggedUser") User user, @PathVariable Integer id) {
        try {
            return new ResponseEntity(userListService.deleteList(user, id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
    
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity update(@RequestAttribute("loggedUser") User user, @RequestBody UpdateListRequest request) {
        try {
            return new ResponseEntity(userListService.updateList(user, request.getId(), request.getName(), request.getCountriesIds()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
}
