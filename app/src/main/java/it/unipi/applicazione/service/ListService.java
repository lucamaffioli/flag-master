package it.unipi.applicazione.service;

import com.google.gson.reflect.TypeToken;
import it.unipi.applicazione.model.Country;
import it.unipi.applicazione.model.ListStatsDTO;
import it.unipi.applicazione.model.UpdateListRequest;
import it.unipi.applicazione.model.UserListDTO;
import java.util.List;

/**
 * Servizi per gestione liste.
 * @author lucamaffioli
 */
public class ListService extends APIclient {
    
    public List<UserListDTO> getAllLists() throws Exception {
        return get("/list/my-lists", new TypeToken<List<UserListDTO>>(){}.getType());       
    }
    
    public String deleteList(int listId) throws Exception {
        return post("/list/delete/" + listId, null, String.class);
    }
    
    public String saveList(UpdateListRequest updateListRequest) throws Exception {
        return post("/list/update", updateListRequest, String.class);
    }
    
    public List<String> getRegions() throws Exception {
        return get("/country/regions", new TypeToken<List<String>>(){}.getType());
    }
    
    public List<Country> getCountriesByRegion(String region) throws Exception {
        return get("/country/countries/" + region, new TypeToken<List<Country>>(){}.getType());
    }
    
    public List<ListStatsDTO> getStatistics() throws Exception {
        return get("/score/stats", new TypeToken<List<ListStatsDTO>>(){}.getType());
    }
   
}
