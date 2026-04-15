module it.unipi.applicazione {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.google.gson;
    requires java.net.http;

    
    opens it.unipi.applicazione.model to javafx.base, com.google.gson;
    //opens it.unipi.applicazione.model to com.google.gson;
    opens it.unipi.applicazione to javafx.fxml;
    opens it.unipi.applicazione.controller to javafx.fxml;
    exports it.unipi.applicazione;
    
}
