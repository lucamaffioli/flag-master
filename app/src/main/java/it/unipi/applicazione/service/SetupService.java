package it.unipi.applicazione.service;

/**
 * Servizio per inizializzazione.
 * @author lucamaffioli
 */
public class SetupService extends APIclient {
    
    public String inizializza() throws Exception {
        return post("/setup/inizializza", null, String.class);
    }
}
