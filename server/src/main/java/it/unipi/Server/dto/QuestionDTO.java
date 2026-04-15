package it.unipi.Server.dto;

import java.util.List;

/**
 * Struttura di una domanda del gioco.
 * @author lucamaffioli
 */
public class QuestionDTO {
    
    // Url immagine bandiera.
    private String flagURL;
    // Risposta corretta.
    private String correctOption;
    // Opzioni disponibili (compresa quella corretta).
    private List<String> options;

    public QuestionDTO(String flagURL, String correctOption, List<String> options) {
        this.flagURL = flagURL;
        this.correctOption = correctOption;
        this.options = options;
    }

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
