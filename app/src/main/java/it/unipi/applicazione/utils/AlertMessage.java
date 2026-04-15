package it.unipi.applicazione.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert;

/**
 * Mostra alert per avvisi.
 * @author lucamaffioli
 */
public class AlertMessage {
    
    private static int WIDTH = 700;
    
    /**
     * Mosta Alert di warning.
     * @param title 
     * @param message 
     * @return true se premuto button YES, false altrimenti.
     */
    public static boolean warning(String title, String message) {
        
        Alert alert = new Alert(Alert.AlertType.WARNING, 
                message,
                ButtonType.YES, ButtonType.NO);
        
        alert.setTitle(title);
        alert.setHeaderText(null); 
        
        alert.getDialogPane().setStyle(
            "-fx-font-size: 24px;" +
            "-fx-base: #F9FAFB;" +    
            "-fx-font-family: 'System';" 
        );

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(WIDTH); 
        alert.getDialogPane().setPrefWidth(WIDTH);
        
        alert.showAndWait();
        
        return (alert.getResult() == ButtonType.YES);
    }
    
    /**
     * Mosta Alert di info.
     * @param title 
     * @param message 
     */
    public static void info(String title, String message) {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                message);
        
        alert.setTitle(title);
        alert.setHeaderText(null); 
        
        alert.getDialogPane().setStyle(
            "-fx-font-size: 24px;" +
            "-fx-base: #F9FAFB;" +     
            "-fx-font-family: 'System';" 
        );

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(WIDTH); 
        alert.getDialogPane().setPrefWidth(WIDTH);
        
        alert.showAndWait();
    }
}
