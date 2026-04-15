package it.unipi.applicazione.model;

/**
 * Risultato di una domanda.
 * @author lucamaffioli
 */
public class GameResult {
    
    // Oggetto della domanda.
    private String flagUrl;
    // Risposta corretta.
    private String correctOption;
    // Risposta fornita dall'utente.
    private String userOption;
    // Se l'utente ha fornito la risposta corretta o meno.
    private boolean correct;

    public GameResult(String flagUrl, String correctOption, String userOption) {
        this.flagUrl = flagUrl;
        this.correctOption = correctOption;
        this.userOption = userOption;
        this.correct = correctOption.equals(userOption);
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getUserOption() {
        return userOption;
    }

    public void setUserOption(String userOption) {
        this.userOption = userOption;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
    
}
