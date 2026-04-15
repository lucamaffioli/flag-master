package it.unipi.applicazione.controller;

import it.unipi.applicazione.App;
import it.unipi.applicazione.model.AddScoreRequest;
import it.unipi.applicazione.model.GameResult;
import it.unipi.applicazione.model.QuestionDTO;
import it.unipi.applicazione.model.UserListDTO;
import it.unipi.applicazione.service.GameService;
import it.unipi.applicazione.utils.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller di game.fxml.
 * @author lucamaffioli
 */
public class GameController {
    
    private static final Logger logger = LogManager.getLogger(GameController.class);
    
    @FXML private Label progressLabel;
    @FXML private ImageView flagImage;
    @FXML private ToggleGroup answersGroup;
    @FXML private Label errorLabel;
    @FXML private Button nextButton;
    
    @FXML private RadioButton option1;
    @FXML private RadioButton option2;
    @FXML private RadioButton option3;
    
    private final GameService gameService = new GameService();
    
    // Stato della partita.
    private List<QuestionDTO> questions = new ArrayList<>();
    private List<GameResult> userResults = new ArrayList<>();
    private int currentQuestionIndex = 0;
    
    /**
     * Funzione di inizializzazione.
     */
    @FXML
    public void initialize() {
        UserListDTO selectedList = Session.getInstance().getSelectedList();
        if (selectedList != null) {
            loadQuestions(selectedList.getId());
        } else {
            showError("No list selected!");
        }
    }
    
    /**
     * Caricamento domande 
     * @param listId 
     */
    private void loadQuestions(int listId) {
        
        Task<List<QuestionDTO>> task = new Task<>() {
            @Override
            protected List<QuestionDTO> call() throws Exception {
                return gameService.getQuestions(listId);
            }
        };
        
        task.setOnSucceeded(event -> {
            logger.info("Caricamento domande andato a buon fine.");
            questions = task.getValue();
            if (questions.isEmpty()) {
                showError("This list has no questions available.");
                nextButton.setDisable(true);
            } else {
                currentQuestionIndex = 0;
                showQuestion(currentQuestionIndex);
            }
        });
        
        task.setOnFailed(event -> {
            logger.error("Errore durande il caricamento delle domande.");
            showError("Error loading questions.");
        });
        
        new Thread(task).start();
    }
    
    /**
     * Mosta domanda numero index.
     * @param index 
     */
    private void showQuestion(int index) {
        
        errorLabel.setVisible(false);
        // Deselezione scelta.
        answersGroup.selectToggle(null);
        
        QuestionDTO question = questions.get(index);
        
        progressLabel.setText("Question " + (index + 1) + " of " + questions.size());
        
        if (question.getFlagURL() != null) {
            flagImage.setImage(new Image(question.getFlagURL(), true)); 
        }
        
        List<RadioButton> radios = List.of(option1, option2, option3);
        List<String> options = question.getOptions();
        
        for (int i = 0; i < radios.size(); i++) {
            RadioButton rb = radios.get(i);
            rb.setText(options.get(i));
            rb.setVisible(true);
            rb.setDisable(false);
        }
        
        if (index == questions.size() - 1) {
            nextButton.setText("FINISH GAME");
            nextButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black;");
        } else {
            nextButton.setText("NEXT QUESTION");
        }
    }
    
    /**
     * Gesiotne Button Next/Finish.
     */
    @FXML
    private void handleNext() {
        
        RadioButton selectedRadio = (RadioButton) answersGroup.getSelectedToggle();
        
        if (selectedRadio == null) {
            showError("Please select an option");
            return;
        }

        QuestionDTO currentQ = questions.get(currentQuestionIndex);
        String userAnswer = selectedRadio.getText();
        
        // Creazione oggetto risultato ed inserimento nella lista dei risultati.
        GameResult result = new GameResult(
            currentQ.getFlagURL(),
            currentQ.getCorrectOption(),
            userAnswer
        );
        userResults.add(result);

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        } else {
            finishGame();
        }
    }
    
    /**
     * Fine del gioco. Caricamento score e passaggio a schermata risultati.
     */
    private void finishGame() {

        // Conteggio risposte corrette.
        int correctAnswers = 0;
        for (GameResult result : userResults) {
            if (result.isCorrect()) {
                correctAnswers++;
            }
        }

        // Creazione oggetto per passaggio score al server.
        AddScoreRequest addScoreRequest = new AddScoreRequest();
        addScoreRequest.setListId(Session.getInstance().getSelectedList().getId());
        addScoreRequest.setPoints(correctAnswers);
        addScoreRequest.setTotalQuestions(questions.size());

        nextButton.setDisable(true);
        nextButton.setText("SAVING...");

        Task<String> saveTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return gameService.addScore(addScoreRequest);
            }
        };

        saveTask.setOnSucceeded(event -> {
            logger.info("Invio risultati andato a buon fine.");
            // Salvataggio dei risultati in sessione per passarli alla prossima schermata
            Session.getInstance().setLastGameResults(userResults);
            try {
                App.setRoot("result");
            } catch (IOException e) {
                logger.error("Errore durante il caricamento di result.fxml");
                showError("Error loading results screen.");
            }
        });

        saveTask.setOnFailed(event -> {
            logger.error("Errore durante l'invio dei risultati");
            Throwable ex = saveTask.getException();
            nextButton.setDisable(false); 
            nextButton.setText("FINISH GAME");
            showError("Could not save score: " + ex.getMessage());
        });

        new Thread(saveTask).start();
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
