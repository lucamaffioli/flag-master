package it.unipi.Server.controller;

import it.unipi.Server.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * API per ottenere regioni e relativi paesi.
 * @author lucamaffioli
 */
@Controller
@RequestMapping(path="/api/country")
public class CountryController {
    
    @Autowired
    private CountryService countryService;
    
    @GetMapping(path="/regions")
    @ResponseBody
    public ResponseEntity<String> regions() {
        try {
            return new ResponseEntity(countryService.getRegions(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }
    
    @GetMapping(path="/countries/{region}")
    @ResponseBody
    public ResponseEntity<String> countries(@PathVariable String region) {
        try {
            return new ResponseEntity(countryService.getRegionCountries(region), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }
    
}
