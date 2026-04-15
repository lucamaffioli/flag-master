package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.Country;
import it.unipi.applicazione.model.UpdateListRequest;
import it.unipi.applicazione.model.UserListDTO;
import it.unipi.applicazione.service.ListService;
import it.unipi.applicazione.utils.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller per editList.fxml.
 * @author lucamaffioli
 */
public class EditListController {
    
    private static final Logger logger = LogManager.getLogger(EditListController.class);
    
    @FXML private TextField nameField;
    @FXML private ListView<Country> targetList; 
    @FXML private ListView<Country> sourceList;
    @FXML private ComboBox<String> regionCombo;
    @FXML private Label messageLabel;
    
    private final ObservableList<Country> targetItems = FXCollections.observableArrayList();
    private final ObservableList<Country> sourceItems = FXCollections.observableArrayList();
    
    private final ListService listService = new ListService();
    
    // Lista corrente (in modifica o creazione)
    private UserListDTO currentList;
    
    /**
     * Funzione di inizializzazione.
     */
    @FXML
    public void initialize() {
        
        targetList.setItems(targetItems);
        sourceList.setItems(sourceItems);
        
        targetList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sourceList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        currentList = Session.getInstance().getSelectedList();
        
        // Nome e paesi lista se presenti.
        if (currentList != null) {
            nameField.setText(currentList.getName());
            if (currentList.getCountries() != null) {
                targetItems.addAll(currentList.getCountries());
            }      
        } 
        
        // Caricamento continenti nel combo Box.
        loadCombo();
        
        // Quando viene selezionata una regione devono essere caricati
        // nella source list i paesi relativi.
        regionCombo.setOnAction(event -> {
            String selectedRegion = regionCombo.getValue();
            if (selectedRegion != null) {
                loadCountriesForRegion(selectedRegion);
            }
        });   
    }
    
    /**
     * Gestione Button ADD.
     */
    @FXML
    private void handleAddCountry() {
        
        logger.info("Button ADD premuto.");
        
        // Lista dei paesi selezionati dalla lista suorce.
        List<Country> selected = new ArrayList<>();
        selected.addAll(sourceList.getSelectionModel().getSelectedItems());
        
        if (!selected.isEmpty()) {
            // Ogni paese selezionato viene rimosso dalla source list
            // e aggiunto alla lista corrente.
            for (Country c : selected) {
                sourceItems.remove(c);
                targetItems.add(c);
            }
            // Ordina alfabeticamente la target list per pulizia
            FXCollections.sort(targetItems, (a,b) -> a.getName().compareTo(b.getName()));
        }
    }
    
    /**
     * Gestione Button REMOVE.
     */
    @FXML
    private void handleRemoveCountry() {   
        
        logger.info("Button REMOVE premuto.");
        
        // I paesi selezionati vengono spostati dalla target list alla source list
        // solo se appartiene alla regione attualmente visualizzata, altrimenti 
        // spariscono dalla target list e basta.
        List<Country> selected = new ArrayList<>();
        selected.addAll(targetList.getSelectionModel().getSelectedItems());
        
        if (!selected.isEmpty()) {
            for (Country c : selected) {
                targetItems.remove(c);
                String currentRegion = regionCombo.getValue();
                if (currentRegion != null) {
                    // Per spostare il paese vine ricaricata la lista completa.
                    loadCountriesForRegion(currentRegion);
                }
            }     
        }
    }
    
    /**
     * Gestione Button SAVE.
     */
    @FXML
    private void handleSave() {
        
        logger.info("Button SAVE premuto.");
        
        String listName = nameField.getText();
        if (listName.isEmpty() || listName == null) {
            showMessage("Enter list name");
            return;
        }
        
        UpdateListRequest request = new UpdateListRequest();
        
        request.setName(listName);
        
        // Se la lista corrente è null, la lista deve essere creata.
        if (currentList != null) {
            request.setId(currentList.getId());
        } else {
            request.setId(0);
        }
        
        List<Integer> countryIds = new ArrayList<>();
        
        // Salvataggio degli IDs dei paesi presenti nella target list.
        for (Country country : targetItems) {
            countryIds.add(country.getId());
        }
        
        request.setCountriesIds(countryIds);
        
        Task<String> saveTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return listService.saveList(request);
            }
        };
        
        saveTask.setOnSucceeded(event -> {
            // Rimozione lista selezionata dalla sessione.
            Session.getInstance().setSelectedList(null);
            try {
                App.setRoot("lists");
            } catch (IOException e) {
                logger.error("Errore durante il caricamento di lists.fxml.");
                showMessage("Error during lists page loading");
            }
        });
        
        saveTask.setOnFailed(event -> {
            logger.error("Errore durante il salvataggio della lista.");
            Throwable exception = saveTask.getException();
            showMessage(exception.getMessage());
        });
        
        new Thread(saveTask).start();
    }
    
    
    /**
     * Caricamento continenti e inserimento nel combo Box.
     */
    private void loadCombo() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return listService.getRegions();
            }
        };
        
        task.setOnSucceeded(event -> {
            logger.info("Continenti scaricati con successo");
            regionCombo.getItems().setAll(task.getValue());
        });
        
        task.setOnFailed(event -> {
            logger.error("Errore durante il caricamento dei continenti");
        });
        
        new Thread(task).start();
    }
    
    /**
     * Caricamento paesi e inserimento nella source list.
     * @param region continente dei paesi da scaricare.
     */
    private void loadCountriesForRegion(String region) {
        
        sourceList.setDisable(true);
        
        Task<List<Country>> task = new Task<>() {    
            @Override
            protected List<Country> call() throws Exception {
                return listService.getCountriesByRegion(region);
            }
        };
        
        task.setOnSucceeded(event -> {
            logger.info("Paesi caricati con successo.");
            sourceList.setDisable(false);
            sourceItems.clear();
            
            // Non vengono mostrati nell'elenco sorgente i paesi già presenti nella lista (di destra).
            for (Country c : task.getValue()) {
                if (!targetItems.contains(c)) { 
                    sourceItems.add(c);
                }
            }
            
            // Ordinamento in ordine alfabetico dei paesi.
            FXCollections.sort(sourceItems, (a,b) -> a.getName().compareTo(b.getName()));
            
        });
        
        task.setOnFailed(e -> {
            sourceList.setDisable(false);
            logger.error("Errore durante il caricamento dei paesi");
            showMessage("Error during countries loading: " + task.getException().getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Gestione Button Back.
     */
    @FXML
    private void handleBack() {
        logger.info("Button GO BACK premuto.");
        try {
            // Rimozione della lista selezionata dalla sessione.
            Session.getInstance().setSelectedList(null);
            App.setRoot("lists");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di lists.fxml");
            showMessage("Loading page error");
        }
        
    }

    /**
     * Visualizza messaggio nel label dedicato.
     * @param message 
     */
    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
    
}
