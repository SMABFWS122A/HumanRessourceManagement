package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.service.ZeitbuchungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;

@RestController
public class ZeitbuchungController {

    @Autowired
    private ZeitbuchungService service;

    @PostMapping(value = "/zeitbuchung")
    public Integer addZeitbuchung(@RequestBody Zeitbuchung zeitbuchung){
        return service.addZeitbuchung(zeitbuchung);
    }

    @GetMapping(value = "/zeitbuchungen/{personalnummer}/{datum}")
    public List<Zeitbuchung> getAllZeitbuchungenByPersonalnummerAndDatum(@PathVariable Integer personalnummer, @PathVariable Date datum){
        return service.getAllZeitbuchungenByPersonalnummerAndDatum(personalnummer, datum);
    }

    @GetMapping(value = "/zeitbuchung/{id}")
    public Zeitbuchung getZeitbuchungById(@PathVariable Integer id){
        return service.getZeitbuchungById(id).orElseThrow(
                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found"));
    }

    @PutMapping(value = "/zeitbuchung")
    public Integer updateZeitbuchung(@RequestBody Zeitbuchung zeitbuchung){
        return service.updateZeitbuchung(zeitbuchung);
    }
    @DeleteMapping(value = "/zeitbuchung/{id}")
    public void deleteZeitbuchundById(@PathVariable Integer id){
        service.deleteZeitbuchungById(id);
    }
}
