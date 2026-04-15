package it.unipi.applicazione.model;

/**
 * Oggetto da inviare al server per aggiunta punteggio a fine partita.
 * @author lucamaffioli
 */
public class AddScoreRequest {
    
    private Integer listId;
    private int points;
    private int totalQuestions;

    public AddScoreRequest() {}

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
}
