package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
import com.smabfws122a.humanressourcemanagement.service.FehlermeldungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class FehlermeldungController {

    @Autowired
    FehlermeldungService service;

    @PostMapping(value = "/fehlermeldung")
    public Integer addFehlermeldung(@RequestBody Fehlermeldung fehlermeldung){
        return service.addFehlermeldung(fehlermeldung);
    }

    @GetMapping(value = "/fehlermeldung")
    public List<Fehlermeldung> getAllFehlermeldung(){
        return service.getAllFehlermeldung();
    }

    @GetMapping(value = "/fehlermeldung/{personalnummer}")
    public Fehlermeldung getLatestFehlermeldungbyPersonalnummer(@PathVariable Integer personalnummer){
        return service.getLatestFehlermeldungbyPersonalnummer(personalnummer).orElseThrow(
                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found")
        );
    }

    @PutMapping(value = "/fehlermeldung")
    public Integer updateFehlermeldung(@RequestBody Fehlermeldung fehlermeldung){
        return service.updateFehlermeldung(fehlermeldung);
    }

    @DeleteMapping(value = "/fehlermeldung/{personalnummer}")
    public void deleteFehlermeldungbyPersonalnummer(@PathVariable Integer personalnummer){
        service.deleteFehlermeldungbyPersonalnummer(personalnummer);
    }
}

