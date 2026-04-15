package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.ListStatsDTO;
import it.unipi.applicazione.service.ListService;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller per statistics.fxml.
 * @author lucamaffioli
 */
public class StatsController {
    
    private static final Logger logger = LogManager.getLogger(StatsController.class);
    
    @FXML private TableView<ListStatsDTO> statsTable;
    
    @FXML private TableColumn<ListStatsDTO, String> colName;
    @FXML private TableColumn<ListStatsDTO, Integer> colGames;
    @FXML private TableColumn<ListStatsDTO, Integer> colQuestions;
    @FXML private TableColumn<ListStatsDTO, Integer> colPoints;
    @FXML private TableColumn<ListStatsDTO, Double> colAccuracy;
    
    @FXML private Label messageLabel;

    private final ListService listService = new ListService();
    private final ObservableList<ListStatsDTO> statsData = FXCollections.observableArrayList();
    
    /**
     * Funzione di inizializzazione.
     */
    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("listName"));
        colGames.setCellValueFactory(new PropertyValueFactory<>("totGames"));
        colQuestions.setCellValueFactory(new PropertyValueFactory<>("totQuestions"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("totPoints"));
        colAccuracy.setCellValueFactory(new PropertyValueFactory<>("accuracy"));
        
        // Aggiunta segno % e colori a arametro accuracy.
        colAccuracy.setCellFactory(column -> new TableCell<ListStatsDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " %");
                    
                    // Colori accuratezza.
                    if (item > 70) setStyle("-fx-text-fill: green; -fx-alignment: CENTER; -fx-font-weight: bold;");
                    else if (item < 40) setStyle("-fx-text-fill: red; -fx-alignment: CENTER; -fx-font-weight: bold;");
                    else setStyle("-fx-text-fill: black; -fx-alignment: CENTER; -fx-font-weight: bold;");
                }
            }
        });
        
        statsTable.setItems(statsData);
        // Se non sono presenti statistiche.
        statsTable.setPlaceholder(new Label("No statistics available"));

        // Caricamento statistiche utente.
        loadStats();
    }
    
    /**
     * Caricamento statistiche utente.
     */
    private void loadStats() {
        
        Task<List<ListStatsDTO>> task = new Task<>() {
            @Override
            protected List<ListStatsDTO> call() throws Exception {
                return listService.getStatistics();
            }
        };
        
        task.setOnSucceeded(event -> {
            logger.info("Caricamento statistiche riuscito con successo");
            statsData.clear();
            statsData.addAll(task.getValue());
            messageLabel.setVisible(false);
        });
        
        task.setOnFailed(event -> {
            logger.error("Errore durante il caricamento della statistiche");
            Throwable exception = task.getException();
            showmessage(exception.getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Gesione Button Home.
     */
    @FXML
    private void switchToHome() {
        logger.info("Button home premuto, cambio schermata.");
        try {
            App.setRoot("menu"); 
        } catch (IOException e) {
            logger.error("Errore durante il caricamento della schermata menu.fxml.");
            showmessage("Impossible to load the menu");
        }
    }
    
    /**
     * Mostra nel Label dedicato un messaggio.
     * @param message 
     */
    private void showmessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
    
}
