package it.unipi.Server.service;

import it.unipi.Server.dto.UserListDTO;
import it.unipi.Server.model.Country;
import it.unipi.Server.model.User;
import it.unipi.Server.model.UserList;
import it.unipi.Server.repository.CountryRepository;
import it.unipi.Server.repository.UserListRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author lucamaffioli
 */
@ExtendWith(MockitoExtension.class)
public class UserListServiceTest {
    
    // Creazione database finti (Mock), per testing.
    @Mock
    private UserListRepository userListRepository;
    
    @Mock
    private CountryRepository countryRepository;

    // Mock dentro il service da testare
    @InjectMocks
    private UserListService instance;
    
    public UserListServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test del metodo getListByUsers.
     * Verifica che il service chiami il repository correttamente.
     */
    @Test
    public void testGetListByUsers() {
        System.out.println("getListByUsers");
        
        User user = new User();
        user.setUsername("pippo");
        
        // Simuliamo che il DB restituisca una lista vuota.
        List<UserList> dbLists = new ArrayList<>();
        when(userListRepository.findByOwner(user)).thenReturn(dbLists);

        List<UserListDTO> result = instance.getListByUsers(user);

        assertNotNull(result);
        verify(userListRepository).findByOwner(user);
    }

    /**
     * Test del metodo deleteList.
     * Verifica che venga chiamato il delete sul repository.
     */
    @Test
    public void testDeleteList() {
        System.out.println("deleteList");
        
        User user = new User(); 
        user.setUsername("pippo");
        Integer idList = 100;
        
        UserList listToDelete = new UserList();
        listToDelete.setId(idList);
        listToDelete.setOwner(user);
        
        // Inizializziamo la lista dei paesi per evitare NullPointerException nel clear()
        listToDelete.setCountries(new ArrayList<>()); 
        
        when(userListRepository.findById(idList)).thenReturn(Optional.of(listToDelete));

        // Supponiamo che il metodo restituisca una stringa di conferma
        String result = instance.deleteList(user, idList); 

        // Verifica che abbia provato a salvare (per sganciare le relazioni) e poi eliminare
        verify(userListRepository).delete(listToDelete);
        assertNotNull(result);
    }

    /**
     * Test del metodo updateList.
     * Verifica l'aggiornamento del nome e la logica retainAll dei paesi.
     */
    @Test
    public void testUpdateList() {
        System.out.println("updateList");
        
        User user = new User(); 
        user.setUsername("pippo");
        Integer idList = 50;
        // Nuovo nome lista.
        String newName = "New List";
        
        Country c1 = new Country(); c1.setId(1); c1.setName("Italy");
        Country c2 = new Country(); c2.setId(2); c2.setName("France");
        Country c3 = new Country(); c3.setId(3); c3.setName("Spain");
        
        // La lista inizialmente contiene Italia, e  Francia.
        UserList existingList = new UserList();
        existingList.setId(idList);
        existingList.setName("Old Name");
        existingList.setOwner(user);
        existingList.setCountries(new ArrayList<>(Arrays.asList(c1, c2)));
        
        // Array con gli id dei paesi richiesti.
        List<Integer> newCountriesIds = Arrays.asList(1, 3);
        
        // Simulazione comportamento del Repository.
        when(userListRepository.findById(idList)).thenReturn(Optional.of(existingList));
        when(countryRepository.findAllById(newCountriesIds)).thenReturn(Arrays.asList(c1, c3));

        String result = instance.updateList(user, idList, newName, newCountriesIds);

        // Verifica il cambio nome.
        assertEquals(newName, existingList.getName());
        
        // Verifica la logica dei paesi.
        List<Country> finalCountries = existingList.getCountries();
        assertEquals(2, finalCountries.size(), "La lista deve avere 2 paesi");
        assertTrue(finalCountries.contains(c1), "Deve contenere Italia");
        assertTrue(finalCountries.contains(c3), "Deve contenere Spagna (Nuova)");
        assertFalse(finalCountries.contains(c2), "Non deve contenere Francia (Rimossa)");
        
        // Verifica salvataggio.
        verify(userListRepository).save(existingList);
    }
    
}
