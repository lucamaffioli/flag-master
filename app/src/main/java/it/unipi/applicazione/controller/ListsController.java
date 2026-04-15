package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.UserListDTO;
import it.unipi.applicazione.service.ListService;
import it.unipi.applicazione.utils.AlertMessage;
import it.unipi.applicazione.utils.Session;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lucamaffioli
 */
public class ListsController {
    
    private static final Logger logger = LogManager.getLogger(ListsController.class);
    
    @FXML private ListView<UserListDTO> lists;
    @FXML private Label messageLabel;
    
    // Fonte dati per la ListView.
    private final ObservableList<UserListDTO> observableList = FXCollections.observableArrayList();
 
    private final ListService listService = new ListService();
    
    @FXML
    public void initialize() {
        
        // Collegamento.
        lists.setItems(observableList);
        
        // Caricamento dati tramite task.
        refreshLists();
        
        // Doppio click su elemento della lista per passare a schemata modifica.
        lists.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                UserListDTO selected = lists.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    goToEditList(selected);
                }
            }
        });
        
        // Tasto destro su un elemento per comparsa del menu.
        ContextMenu contextMenu = new ContextMenu();
        
        contextMenu.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        
        String itemStyle = 
            "-fx-font-size: 24px;" +       
            "-fx-font-weight: bold;" +     
            "-fx-text-fill: #333333;" +    
            "-fx-padding: 10 20 10 20;";

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> {
            UserListDTO selected = lists.getSelectionModel().getSelectedItem();
            if (selected != null) {
                goToEditList(selected);
            }
        });
        
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            UserListDTO selected = lists.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handleDeleteList(selected);
            }
        });
        
        editItem.setStyle(itemStyle);
        deleteItem.setStyle(itemStyle);
        
        contextMenu.getItems().addAll(editItem, deleteItem);
        
        // Collegamento.
        lists.setContextMenu(contextMenu);
    }
    
    /**
     * Carica elenco delle liste dell'utente.
     */
    private void refreshLists() {
        
        // Disabilitazione lista durante il caricamento dei dati.
        lists.setDisable(true);
        
        Task<List<UserListDTO>> refreshTask = new Task<>() {
            @Override
            protected List<UserListDTO> call() throws Exception {
                return listService.getAllLists();
            }
        };
        
        refreshTask.setOnSucceeded(event -> {
            lists.setDisable(false);
            observableList.clear();
            observableList.addAll(refreshTask.getValue());
            logger.info("Elenco liste scaricato con successo.");
            messageLabel.setVisible(false);
        });
        
        refreshTask.setOnFailed(event -> {
            lists.setDisable(false);
            logger.error("Errore durante lo scaricamento delle liste.");
            showMessage(refreshTask.getException().getMessage());
        });
        
        new Thread(refreshTask).start();
    }
    
    
    /**
     * Handle per eliminazione lista.
     * @param listToDelete 
     */
    private void handleDeleteList(UserListDTO listToDelete) {
        
        if (!AlertMessage.warning("", "Delete the list " + listToDelete.getName() + "?")) {
            return;
        }
  
        Task<String> deleteTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return listService.deleteList(listToDelete.getId());
            }
        };

        deleteTask.setOnSucceeded(event -> {
            // Rimozione della lista.
            logger.info("Lista rimossa con successo");
            observableList.remove(listToDelete);
            showMessage("List deleted");
        });

        deleteTask.setOnFailed(event -> {
            logger.error("Errore durante l'eliminazione della lista");
            showMessage(deleteTask.getException().getMessage());
        });

        new Thread(deleteTask).start();
    }
    
    
    /**
     * Gestione Button NEW list.
     */
    @FXML
    public void handleNewList() {
        logger.info("Button new premuto");
        goToEditList(null);
    }

    /**
     * Gestione Button Home.
     */
    @FXML
    public void switchToHome() {
        logger.info("Button Home premuto, torno alla schermata principale");
        try {
            App.setRoot("menu");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di menu.fxml");
            showMessage("Impossible to load the home page");
        }

    }
    
    /**
     * Setta il valore della lista da modificare in Session e va alla schermata editList.
     * @param listToEdit struttura lista da modificare, null se la lista è da creare.
     */
    private void goToEditList(UserListDTO listToEdit) {
        try {
            Session.getInstance().setSelectedList(listToEdit);
            App.setRoot("editList");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di editList.fxml");
            showMessage("Impossible to load the edit page");
        }
    }
    
    /**
    * Mostra nel Label dedicato un messaggio informativo o di errore.
    * @param message 
    */
    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
    
}
