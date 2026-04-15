package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.User;
import it.unipi.applicazione.service.AuthService;
import it.unipi.applicazione.utils.Session;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller per menu.fxml
 * @author lucamaffioli
 */
public class HomeController {
    
    private static final Logger logger = LogManager.getLogger(HomeController.class);
    
    @FXML private Label errorLabel;
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private VBox vbox;
    
    private final AuthService authService = new AuthService();
   
    /**
     * Funzione di inizializzazione.
     * Invocata prima del caricamento.
     */
    @FXML
    private void initialize()  {
        User user = Session.getInstance().getLoggedUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        } else {
            welcomeLabel.setText("Welcome, player!");
        }   
    }
    
    /**
     * Gestione button mylists.
     */
    @FXML
    private void switchToLists() {
        logger.info("Bottone MY LISTS premuto, passo a schermata lists");
        try {
            App.setRoot("lists");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di lists.fxml.");
            showError("Impossible to load the lists page");
        }
    }
    
    /**
     * Gestione Button Play.
     */
    @FXML
    private void switchToStartGame() {
        logger.info("Bottone PLAY premuto, passo a schermata startGame");
        try {
            App.setRoot("playSelection");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di playSelection.fxml.");
            showError("Impossible to load the game page");
        }
    }
    
    /**
     * Gestione button logout.
     */
    @FXML
    private void handleLogout() {
        
        setLoadingState(true);
        
        Task<String> loginTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return authService.logout();
            }
        };
        
        loginTask.setOnSucceeded(event ->{
            setLoadingState(false);
            try {
                logger.info("Logout effettuato. Passaggio a schermata menù.");
                App.setRoot("login");
            } catch (IOException e) {
                logger.error("Errore durante il caricamento di login.fxml.");
                showError("Impossible to load the menu");
            }
        });
        
        
        loginTask.setOnFailed(event -> {
            setLoadingState(false);
            Throwable exception = loginTask.getException();
            logger.error("Errore durante il logout");
            showError(exception.getMessage());
        });
        
        new Thread(loginTask).start();
    }
    
    
    /**
     * Gestione button Statistics.
     */
    @FXML
    private void switchToStatistics() {
        logger.info("Bottone STATISTICS premuto, passo a schermata statistics");
        try {
            App.setRoot("statistics");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di statistics.fxml.");
            showError("Impossible to load the statistcs");
        }
    }
    
    /**
     * Mostra nel Label dedicato un messaggio di errore.
     * @param message 
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    /**
     * @param isLoading 
     */
    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            logoutButton.setText("loading...");
            vbox.setDisable(true);
            errorLabel.setVisible(false);
        } else {
            logoutButton.setText("logout");
            vbox.setDisable(false);
        }
    }
    
}
