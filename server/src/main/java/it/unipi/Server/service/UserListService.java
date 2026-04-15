package it.unipi.Server.service;

import it.unipi.Server.dto.UserListDTO;
import it.unipi.Server.model.Country;
import it.unipi.Server.model.User;
import it.unipi.Server.model.UserList;
import it.unipi.Server.repository.CountryRepository;
import it.unipi.Server.repository.UserListRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Gesione liste di paesi.
 * @author lucamaffioli
 */
@Service
public class UserListService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserListService.class);
    
    @Autowired
    private UserListRepository userListRepository;
    
    @Autowired
    private CountryRepository countryRepository;
    
    /**
     * Ricerca di tutte le liste appartenenti ad user.
     * @param user.
     * @return List di liste di paesi.
     */
    public List<UserListDTO> getListByUsers(User user) {
        List<UserList> lists = userListRepository.findByOwner(user);
        
        // Necessario per mappare un java Bean in un DTO compatibile con json per il trasferimento.
        return StreamSupport.stream(lists.spliterator(), false)
            .map(list -> new UserListDTO(
                list.getId(), 
                list.getName(), 
                list.getCountries()
            ))
            .toList();
        
    }
    
    /**
     * Metodo per eliminazione di una lista.
     * @param user utente proprietario della lista.
     * @param idList lista da eliminare.
     * @return "LIST_DELETED" in caso di successo.
     * Vengono alzate eccezioni con messaggi in caso di errore.
     */
    public String deleteList(User user, Integer idList) {
        
        Optional<UserList> listOpt = userListRepository.findById(idList);
        
        if (listOpt.isEmpty()) {
            logger.warn("Id lista non valido");
            throw new RuntimeException("LIST_NOT_FOUND");
        }
        
        UserList list = listOpt.get();
        
        if (!list.getOwner().getUsername().equals(user.getUsername())) {
            logger.warn("La lista non è di proprietà dell'utente user");
            throw new RuntimeException("LIST_NOT_AVAILABLE");
        }
        
        // Rimozione preventiva dei paesi, questo consente di non avere problemi
        // durante la rimozione nella join table.
        list.getCountries().clear();
        userListRepository.save(list);
        
        // Rimozione della lista dal db.
        userListRepository.delete(list);
        
        return "LIST_DELETED";
    }
    
    /**
     * Metodo per aggiornare/creare una lista.
     * @param user utente proprietario.
     * @param idList della lista da modificare, se 0 la lista viene creata.
     * @param name nuovo nome della lista.
     * @param countriesIds Lista di id dei aesi contenuti nella lista aggiornata.
     * @return "LIST_CREATED" o "LIST_UPDATED" in caso di successo.
     * Vengono alzate eccezioni con messaggi in caso di errore.
     */
    public String updateList(User user, Integer idList, String name, List<Integer> countriesIds) {
        
        // Se l'id della lista è zero creo la lista con i paesi passati.
        if (idList == 0) {
            UserList userList = new UserList();
            userList.setOwner(user);
            userList.setName(name);
            
            Iterable<Country> countriesIterable = countryRepository.findAllById(countriesIds);
            List<Country> newCountries = new ArrayList<>();
            countriesIterable.forEach(newCountries::add);
             
            userList.setCountries(newCountries);
            
            userListRepository.save(userList);
            
            return "LIST_CREATED";
        }
        
        Optional<UserList> listOpt = userListRepository.findById(idList);
        
        if (listOpt.isEmpty()) {
            logger.warn("Id lista non valido");
            throw new RuntimeException("LIST_NOT_FOUND");
        }
        
        UserList list = listOpt.get();
        
        if (!list.getOwner().getUsername().equals(user.getUsername())) {
            logger.warn("La lista non è di proprietà dell'utente user");
            throw new RuntimeException("LIST_NOT_AVAILABLE");
        }
        
        // Lista di paesi associati alla lista.
        List<Country> current = list.getCountries();
        
        // Recupero paesi relativi agli id passati dall'utente (paesi nuovi).
        Iterable<Country> countriesIterable = countryRepository.findAllById(countriesIds);
        // Passaggio da Iterable e List per poter utilizzare i metodi.
        List<Country> newCountries = new ArrayList<>();
        countriesIterable.forEach(newCountries::add);
        
        /**
         * Il metodo mantiene il current solo i paesi anche presenti in newCountries.
         * Rimozione dalla lista current dei paesi rimossi dall'utente.
         */
        current.retainAll(newCountries);
        
        // Ciclo per inserimento nuovi paesi.
        for (Country c: newCountries) {
            if (!current.contains(c)) {
                current.add(c);
            }
        }  
       
        // Aggiornamento nome nel caso fosse cambiato.
        list.setName(name);
        
        userListRepository.save(list);

        return "LIST_UPDATED";   
    }
}
