package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.service.AuthService;
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
 * Controller per register.fxml.
 * @author lucamaffioli
 */
public class RegisterController {
    
    private static final Logger logger = LogManager.getLogger(RegisterController.class);
    
    private AuthService authService = new AuthService();
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button signupButton;
    @FXML private VBox signupVbox;
    
    /**
     * Gestione button signup
     * @throws IOException 
     */
    @FXML
    private void handleSignup() throws IOException {
        
        logger.info("Botton signup premuto");
        
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill all fields");
            return;
        }
        
        setLoadingState(true);
        
        Task<String> signupTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return authService.signup(username, password);
            }
        };
        
        signupTask.setOnSucceeded(event ->{
            setLoadingState(false);
            String response = signupTask.getValue();
            showMessage(response);
            try {
                App.setRoot("login");
            } catch (IOException e) {
                logger.error("Errore durante il caricamento di login.fxml.");
                showMessage("Impossible to load the login page");
            } 
        });
        
        signupTask.setOnFailed(event ->{
            setLoadingState(false);
            Throwable exception = signupTask.getException();
            logger.error("Errore durante signup.");
            showMessage(exception.getMessage());
        });
        
        new Thread(signupTask).start();      
    }

    /**
     * Gesione button Login
     * @throws IOException 
     */
    @FXML
    private void switchToLogin() {
        logger.info("Button Login Page premuto");
        try {
            App.setRoot("login");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di login.fxml.");
            showMessage("Impossible to load the login page");
        }
        
    }
    
    /**
     * Mostra nel Label dedicato un messaggio informativo.
     * @param message 
     */
    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
    
    /**
     * Disabilita / Riabilita la parte del signup.
     * @param isLoading 
     */
    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            signupButton.setText("loading...");
            signupVbox.setDisable(true);
            messageLabel.setVisible(false);
        } else {
            signupButton.setText("signup");
            signupVbox.setDisable(false);
        }
    }
}