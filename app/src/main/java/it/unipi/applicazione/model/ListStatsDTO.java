package it.unipi.applicazione.model;

/**
 * Statistiche relative alle partite fatte su una lista.
 * @author lucamaffioli
 */
public class ListStatsDTO {
    
    // Informazioni sulla lista.
    private Integer listId;
    private String listName;
    // Partite giocate in totale.
    private int totGames;
    // Numero di domande fatte in totale.
    private int totQuestions;
    // Numero di risposte giuste date in totale.
    private int totPoints;
    // Accuratezza in punti percentuale.
    private double accuracy;

    public ListStatsDTO(Integer listId, String listName, int totGames, int totQuestions, int totPoints) {
        this.listId = listId;
        this.listName = listName;
        this.totGames = totGames;
        this.totQuestions = totQuestions;
        this.totPoints = totPoints;
        
        // Calcolo accuratezza.
        if (this.totQuestions > 0) {
            double r = (double) totPoints / totQuestions * 100;
            this.accuracy = Math.round(r * 100.0) / 100.0;
        } else {
            this.accuracy = 0.0;
        }   
    }
    
    public ListStatsDTO() {}

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getTotGames() {
        return totGames;
    }

    public void setTotGames(int totGames) {
        this.totGames = totGames;
    }

    public int getTotQuestions() {
        return totQuestions;
    }

    public void setTotQuestions(int totQuestions) {
        this.totQuestions = totQuestions;
    }

    public int getTotPoints() {
        return totPoints;
    }

    public void setTotPoints(int totPoints) {
        this.totPoints = totPoints;
    }

    public double getAccuracy() {
        return accuracy;
    } 
    
}
