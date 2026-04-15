package it.unipi.applicazione.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unipi.applicazione.utils.Session;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Servizio per gestione richieste al server.
 * Gli altri servizi estendono questa classe in modo da poterne utilizzare i metodi.
 * Questa organizzazione consente di centralizzare la logica di comunicazione
 * di rete, il parsing json e la gestione degli errori.
 * @author lucamaffioli
 */
public class APIclient {
    
    // Url base per le richieste al server
    private static final String URL = "http://127.0.0.1:8081/api";
    
    private final Gson gson;
    
    private final HttpClient client;
    
    public APIclient() {
        this.gson = new GsonBuilder().create();
        this.client = HttpClient.newHttpClient();
    }
    
    /**
     * Funzione per richieste POST al server.
     * @param endpoint url della richiesta.
     * @param requestBody Oggetto da inviare nel body della richiesta.
     * @param responseClass Classe attesa come ritorno dalla funzione.
     * @return oggetto di tipo responseClass.
     * @throws Exception in caso di errore del server, con messaggio
     * inviato dal server.
     */
    protected <T> T post(String endpoint, Object requestBody, Class<T> responseClass) throws Exception {
        
        // Conversione da oggetto a json.
        String jsonRequest = gson.toJson(requestBody);
        
        // Costruzione della richiesta.
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest));
        
        // Aggiunta token di sessione.
        addToken(builder);
        
        // Invio richiesta e attesta risposta.
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        
        // Controllo dello status code, in caso di errore l'eccezione ha il messaggio di errore ricevuto dal server.
        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new Exception(response.body());
        }
        
        // Se si attende una semplice String come valore di ritorno non è necessario effettuare la
        // la conversione da json a struttura.
        if (responseClass == String.class) {
            return (T)response.body();
        }
        
        return gson.fromJson(response.body(), responseClass);
    }
    
    /**
     * Funzione per richieste GET al server.
     * @param endpoint url della richiesta.
     * @param responseClass Classe attesa come ritorno dalla funzione.
     * @return oggetto di tipo responseClass.
     * @throws Exception in caso di errore del server, con messaggio
     * inviato dal server.
     */
    protected <T> T get(String endpoint, Class<T> responseClass) throws Exception {
        
        // Costruzione della richiesta.
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(URL + endpoint))
                .GET();
        
        // Aggiunta token di sessione.
        addToken(builder);
        
        // Invio richiesta e attesta risposta.
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        
        // Controllo dello status code, in caso di errore l'eccezione ha il messaggio di errore ricevuto dal server.
        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new Exception(response.body());
        }
        
        // Se si attende una semplice String come valore di ritorno non è necessario effettuare la
        // la conversione da json a struttura.
        if (responseClass == String.class) {
            return (T)response.body();
        }
        
        return gson.fromJson(response.body(), responseClass);
    }
    
    /**
     * Overloading funzione get ma con Type generico di ritorno, necessario 
     * quando il tipo di ritorno è complesso (usato in ListService.java).
     * @param endpoint url della richiesta.
     * @param type tipo atteso come ritorno dalla funzione.
     * @return oggetto di tipo type.
     * @throws Exception in caso di errore del server, con messaggio di 
     * inviato dal server.
     */
    protected <T> T get(String endpoint, Type type) throws Exception {
        // Costruzione della richiesta.
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(URL + endpoint))
                .GET();
        
        // Aggiunta token di sessione.
        addToken(builder);
        
        // Invio richiesta e attesta risposta.
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        
        // Controllo dello status code, in caso di errore l'eccezione ha il messaggio di errore ricevuto dal server.
        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new Exception(response.body());
        }
        
        // Se si attende una semplice String come valore di ritorno non è necessario effettuare la
        // la conversione da json a struttura.
        if (type.equals(String.class)) {
            return (T)response.body();
        }
        
        return gson.fromJson(response.body(), type); 
    }
    
    /**
     * Aggiunta del token alla richiesta.
     * @param builder 
     */
    private void addToken(HttpRequest.Builder builder) {
        String token = Session.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            builder.setHeader("Authorization", token);
        }
    }
}
