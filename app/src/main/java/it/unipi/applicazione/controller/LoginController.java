package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.service.AuthService;
import it.unipi.applicazione.service.SetupService;
import it.unipi.applicazione.utils.AlertMessage;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller per login.fxml. 
 * @author lucamaffioli
 */
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private VBox loginVbox;
    @FXML private VBox buttonVbox;
    
    
    private final AuthService authService = new AuthService();
    private final SetupService setupService = new SetupService();
    
    /**
     * Gestione Button login.
     * @throws IOException 
     */
    @FXML
    private void handleLogin() throws IOException {
        
        logger.info("Botton LOGIN premuto");
        
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return;
        }
        
        // Disattivo elementi sulla schermata.
        setLoadingState(true);
        
        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                authService.login(username, password);
                return null;
            }
        };
        
        loginTask.setOnSucceeded(event ->{
            setLoadingState(false);
            try {
                logger.info("Login effettuato. Passaggio a schermata menù.");
                App.setRoot("menu");
            } catch (IOException e) {
                logger.error("Errore durante il passaggio a schermata menù.");
                showError("Impossible to load the menu");
            }
        });
        
        
        loginTask.setOnFailed(event -> {
            logger.error("Task autenticazione login fallito!");
            setLoadingState(false);
            Throwable exception = loginTask.getException();
            showError(exception.getMessage());
        });
        
        new Thread(loginTask).start();
    }

    /**
     * Gestione Button Initialize DB.
     * @throws IOException 
     */
    @FXML
    private void handleInitialize() throws IOException {
        
        logger.info("Botton initialize DB premuto");
           
        // Alert.
        if (!AlertMessage.warning("Initialization", "Are you sure?\nAll data will be lost!")) {
            return;
        }
        
        // Disattivo elementi sulla schermata.
        setLoadingState(true);
        
        Task<String> initTask = new Task<>() {  
            @Override
            protected String call() throws Exception {
                return setupService.inizializza();
            }
        };
        
        initTask.setOnSucceeded(event -> {
            setLoadingState(false);
            String response = initTask.getValue();
            logger.info("Inizializzazione db riuscita.");
            AlertMessage.info("Initialize DataBase", "Success (" + response + ")");
            
        });
        
        initTask.setOnFailed(event ->{
            setLoadingState(false);
            Throwable exception = initTask.getException();
            logger.error("Errore durante l'inizializzazione del DB: {}", exception.getMessage());
            AlertMessage.info("Initialize DataBase", "ERROR (" + exception.getMessage() + ")");
        });
        
        new Thread(initTask).start();
    }
    
    /**
     * Gestione Button SignUp, passaggio a schermata registrazione.
     * @throws IOException 
     */
    @FXML
    private void switchToRegister() {
        logger.info("Bottone SIGNUP HERE premuto, passo a schermata regiser.fxml.");
        try {
            App.setRoot("register");
        } catch (IOException e) {
            logger.error("Errore durente il caricamento di register.fxml");
            showError("Impossible to load the signup page");
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
     * Disabilita / Riabilita componenti schermata.
     * @param isLoading 
     */
    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            loginVbox.setDisable(true);
            buttonVbox.setDisable(true);
            errorLabel.setVisible(false);
        } else {
            loginVbox.setDisable(false);
            buttonVbox.setDisable(false);
        }
    }
    
}
