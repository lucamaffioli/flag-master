package it.unipi.Server.service;

import it.unipi.Server.model.User;
import it.unipi.Server.repository.CountryRepository;
import it.unipi.Server.repository.UserListRepository;
import it.unipi.Server.repository.UserRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.Server.model.Country;
import it.unipi.Server.repository.ScoreRepository;
import it.unipi.Server.utils.Crypto;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setup: pulizia DB, scaricamento dati API.
 * @author lucamaffioli
 */
@Service
public class SetupService {
    
    private static final Logger logger = LoggerFactory.getLogger(SetupService.class);
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserListRepository userListRepository;
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    /**
     * Rimozione dati dal db.
     * Creazione utente di test.
     * Scaricamento e parsing dati da API esterna.
     * @throws MalformedURLException 
     */
    @Transactional
    public void inizializzaDB() throws MalformedURLException {
        
        // Svuoto tabelle.
        scoreRepository.deleteAll();
        userListRepository.deleteAll();
        countryRepository.deleteAll();
        userRepository.deleteAll();
        logger.info("Tabelle db svuotate");
        
        // Url per scaricamento dati.
        String apiUrl = "https://restcountries.com/v3.1/all?fields=name,flags,region,capital";
        
        try {
            
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            
            String inputLine;
            StringBuffer content = new StringBuffer();
            
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            
            // Parsing
            Gson gson = new Gson();
            
            JsonArray rootArray = gson.fromJson(content.toString(), JsonArray.class);
            List<Country> countriesToSave = new ArrayList<>();
            
            // Parsing Json.
            for(JsonElement element : rootArray) {
            
                try {
                
                    JsonObject obj = element.getAsJsonObject();
                    
                    String nome = "";
                    if (obj.has("name") && obj.get("name").isJsonObject()) {
                        nome = obj.get("name").getAsJsonObject().get("common").getAsString();
                    }
                    
                    String regione = "";
                    if (obj.has("region")) {
                        regione = obj.get("region").getAsString();
                    }
                    
                    String flagUrl = "";
                    if (obj.has("flags") && obj.get("flags").isJsonObject()) {
                        flagUrl = obj.get("flags").getAsJsonObject().get("png").getAsString();
                    }
                    
                    // Essendo le capitali una lista, viene preso solo il primo elemento.
                    String capitale = "";
                    if (obj.has("capital") && obj.get("capital").isJsonArray()) {
                        JsonArray capitals = obj.get("capital").getAsJsonArray();
                        if (capitals.size() > 0) {
                            capitale = capitals.get(0).getAsString();
                        }
                    }
                    
                    Country c = new Country(nome, regione, capitale, flagUrl);
                    countriesToSave.add(c);
                
                } catch (Exception e) {
                    logger.error("Errore durante il parsing del file json");
                    throw new RuntimeException("PARSING_ERROR: " + e);
                }

            } 
            
            countryRepository.saveAll(countriesToSave);
            logger.info("Paesi aggiunti al db con successo");
            
        } catch (Exception e) {
            logger.error("Errore durante la connessione a restcountries:"+ e.getMessage());
            throw new RuntimeException(e);
        }  
    }
}
