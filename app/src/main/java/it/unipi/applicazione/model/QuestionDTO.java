package it.unipi.applicazione.model;

import java.util.List;

/**
 * Domanda di una partita.
 * Ricevuta già in questo formato dal server.
 * @author lucamaffioli
 */
public class QuestionDTO {
    
    // Bandiera.
    private String flagURL;
    // Opzione corretta.
    private String correctOption;
    // Lista opzioni (compresa quella corretta).
    private List<String> options;

    public QuestionDTO(String flagURL, String correctOption, List<String> options) {
        this.flagURL = flagURL;
        this.correctOption = correctOption;
        this.options = options;
    }

    public QuestionDTO() {}
    
    public String getFlagURL() {
        return flagURL;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
