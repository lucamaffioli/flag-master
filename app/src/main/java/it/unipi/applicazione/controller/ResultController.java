package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.GameResult;
import it.unipi.applicazione.utils.Session;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller di result.fxml.
 * @author lucamaffioli
 */
public class ResultController {
    
    private static final Logger logger = LogManager.getLogger(ResultController.class);
    
    @FXML private Label errorLabel;
    @FXML private Label scoreLabel;
    @FXML private Label percentageLabel;
    @FXML private TableView<GameResult> resultsTable;
    
    @FXML private TableColumn<GameResult, String> colFlag;
    @FXML private TableColumn<GameResult, String> colUser;
    @FXML private TableColumn<GameResult, String> colCorrect;
    @FXML private TableColumn<GameResult, Boolean> colResult;

    private final ObservableList<GameResult> resultsData = FXCollections.observableArrayList();
    
    /**
     * Metodo di inizializzazione.
     */
    @FXML
    public void initialize() {
        
        errorLabel.setVisible(false);
        
        // Recupero risultati partita salvati precedentemente nella Sessione.
        List<GameResult> lastGame = Session.getInstance().getLastGameResults();
        
        if (lastGame != null) {
            resultsData.addAll(lastGame);
            calculateScore(lastGame);
        } else {
            scoreLabel.setText("No results found.");
            percentageLabel.setText("");
        }
        
        colUser.setCellValueFactory(new PropertyValueFactory<>("userOption"));
        colCorrect.setCellValueFactory(new PropertyValueFactory<>("correctOption"));
        colResult.setCellValueFactory(new PropertyValueFactory<>("correct")); 
        colFlag.setCellValueFactory(new PropertyValueFactory<>("flagUrl"));
        
        // Mostra l'immagine invece dell'URLnella colonna bandiera.
        colFlag.setCellFactory(column -> new TableCell<GameResult, String>() {
             private final ImageView imageView = new ImageView();
             {
                 imageView.setFitHeight(80);
                 imageView.setPreserveRatio(true);
             }
             @Override
             protected void updateItem(String url, boolean empty) {
                 super.updateItem(url, empty);
                 if (empty || url == null) {
                     setGraphic(null);
                 } else {
                     try {
                         // 'true' per caricamento in background.
                         imageView.setImage(new Image(url, true)); 
                         setGraphic(imageView);
                     } catch (Exception e) {
                         setGraphic(null);
                     }
                 }
             }
        });
        
        // Simbolo nella colonna risultato.
        colResult.setCellFactory(column -> new TableCell<GameResult, Boolean>() {
            @Override
            protected void updateItem(Boolean isCorrect, boolean empty) {
                super.updateItem(isCorrect, empty);
                if (empty || isCorrect == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (isCorrect) {
                        setText("✅");
                        // Verde per corretto
                        setStyle("-fx-text-fill: green; -fx-font-size: 24px; -fx-alignment: CENTER;");
                    } else {
                        setText("❌");
                        // Rosso per sbagliato
                        setStyle("-fx-text-fill: red; -fx-font-size: 24px; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        // Se sbagliata la risposta dell'utente viene colorata di rosso.
        colUser.setCellFactory(column -> new TableCell<GameResult, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Accesso alla riga corrente per vedere se era corretta o meno.
                    GameResult currentRow = getTableView().getItems().get(getIndex());
                    if (!currentRow.isCorrect()) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 10;");
                    } else {
                        setStyle("-fx-text-fill: black; -fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 10;");
                    }
                }
            }
        });

        resultsTable.setItems(resultsData);   
    }
    
    /**
     * Funzione per calcolo delle risposte corrette e dell'accuratezza.
     * @param games 
     */
    private void calculateScore(List<GameResult> games) {
        int total = games.size();
        int correct = 0;
        for (GameResult r : games) {
            if (r.isCorrect()) correct++;
        }
        
        scoreLabel.setText("You got " + correct + " out of " + total + "!");
        
        if (total > 0) {
            double percentage = (double) correct / total * 100;
            percentageLabel.setText(String.format("Accuracy: %.0f%%", percentage));
        }
    }
    
    /**
     * Gesione Button Play Again.
     */
    @FXML
    private void handlePlayAgain() {
        logger.info("Button PLAY AGAIN premuto.");
        try {
            // Ricarica la schermata di gioco (UserListDTO è ancora in Sessione).
            App.setRoot("game");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di game.fxml");
            showError("Error loading game.");
        }
    }

    /**
     * Gesione Button List Selection.
     */
    @FXML
    private void goToPalySelection() {
        logger.info("Button List Selection premuto.");
        try {
            App.setRoot("playSelection");
        } catch (IOException e) {
            logger.error("Errore durante il caricamento di playSelection.fxml");
            showError("Error loading list selection screen.");
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
}
