package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Urlaubsbuchung;
import com.smabfws122a.humanressourcemanagement.service.UrlaubsbuchungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UrlaubsbuchungController {

    @Autowired
    UrlaubsbuchungService service;


    @GetMapping(value = "/urlaubsbuchungen")
    public List<Urlaubsbuchung> getAllUrlaubsbuchungen(){
        return service.getAllUrlaubsbuchungen();
    }

    @PostMapping(value = "/urlaubsbuchung")
    public Integer addUrlaubsbuchung(@RequestBody Urlaubsbuchung urlaubsbuchung){
        return service.addUrlaubsbuchung(urlaubsbuchung);
    }

    @GetMapping(value = "/urlaubsbuchung/{id}")
    public Urlaubsbuchung getUrlaubsbuchungById(@PathVariable Integer id){
        return service.getUrlaubsbuchungById(id).orElseThrow(
                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found"));
    }

    @PutMapping(value = "/urlaubsbuchung")
    public Integer updateUrlaubsbuchung(@RequestBody Urlaubsbuchung urlaubsbuchung){
        return service.updateUrlaubsbuchung(urlaubsbuchung);
    }


    @GetMapping(value = "/urlaubsbuchungen/{personalnummer}/{datum}")
    public List<LocalDate>  getUrlaubstageByPersonalnummerAndMonatAndJahr(@PathVariable Integer personalnummer, @PathVariable LocalDate datum){
        return service.getUrlaubstageByPersonalnummerAndMonatAndJahr(personalnummer, datum);
    }

    @GetMapping(value = "/urlaubstage/{personalnummer}/{datum}")
    public Integer  getAnzahlVerfügbareUrlaubstage(@PathVariable Integer personalnummer, @PathVariable LocalDate datum){
        return service.getAnzahlVerfügbareUrlaubstage(personalnummer, datum);
    }
}
