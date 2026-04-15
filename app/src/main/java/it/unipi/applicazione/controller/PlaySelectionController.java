package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.UserListDTO;
import it.unipi.applicazione.service.ListService;
import it.unipi.applicazione.utils.Session;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller di playSelection.fxml
 * @author lucamaffioli
 */
public class PlaySelectionController {
    
    private static final Logger logger = LogManager.getLogger(PlaySelectionController.class);
    
    @FXML private ListView<UserListDTO> availableListsView;
    @FXML private Label messageLabel;

    private final ListService listService = new ListService();
    private final ObservableList<UserListDTO> listsData = FXCollections.observableArrayList();
    
    /**
     * Funzione di inizializzazione.
     */
    @FXML
    public void initialize() {
        
        availableListsView.setItems(listsData);
        // Se non ci sono liste.
        availableListsView.setPlaceholder(new Label("No lists available. Create one first"));

        availableListsView.setCellFactory(param -> new ListCell<UserListDTO>() {
            @Override
            protected void updateItem(UserListDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                    setAlignment(Pos.CENTER_LEFT); 
                    setStyle("-fx-padding: 0 0 0 10;"); 
                }
            }
        });

        // Caricamento liste utente.
        loadLists();
        
    }
    
    /**
     * Caricamento liste utente.
     */
    private void loadLists() {
        
        Task<List<UserListDTO>> task = new Task<>() {
            @Override
            protected List<UserListDTO> call() throws Exception {
                return listService.getAllLists();
            }
        };

        task.setOnSucceeded(e -> {
            logger.info("Caricamento liste andato a buon fine");
            listsData.clear();
            listsData.addAll(task.getValue());
            messageLabel.setVisible(false);
        });

        task.setOnFailed(e -> {
            logger.error("Errore durante il caricamento delle liste");
            Throwable exception = task.getException();
            showMessage(exception.getMessage());
        });

        new Thread(task).start();
    }
    
    /**
     * Gestione Button PLAY.
     */
    @FXML
    private void handleStartGame() {
        
        UserListDTO selectedList = availableListsView.getSelectionModel().getSelectedItem();

        if (selectedList == null) {
            showMessage("Please select a list to start playing!");
            return;
        }
        
        // conteggio paesi presenti nella lista.
        int countryCount = 0;
        if (selectedList.getCountries() != null) {
            countryCount = selectedList.getCountries().size();
        }

        // Gestione errore.
        if (countryCount < 3) {
            showMessage("This list has only " + countryCount + " countries.\nYou need at least 3 countries to play!");
            return;
        }

        // Passaggio al gioco.
        try {
            // Memorizzazione della lista selezionata nella Sessione.
            Session.getInstance().setSelectedList(selectedList);
            App.setRoot("game"); 
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di game.fxml");
            showMessage("Error starting game: " + e.getMessage());
        }
    }
    
    /**
     * Gestione Button Home.
     */
    @FXML
    private void switchToHome() {
        logger.info("Button Home premuto");
        try {
            App.setRoot("menu");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di menu.fxml");
            showMessage(e.getMessage());
        }
    }
    
    /**
    * Mostra nel Label dedicato un messaggio di errore.
    * @param message 
    */
    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
    
}
