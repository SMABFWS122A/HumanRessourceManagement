package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.service.ZeitbuchungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZeitbuchungController {

    @Autowired
    private ZeitbuchungService service;

    @PostMapping(value = "/zeitbuchung")
    public Integer addZeitbuchung(@RequestBody Zeitbuchung zeitbuchung){
        return service.addZeitbuchung(zeitbuchung);
    }

    @GetMapping(value = "/zeitbuchungen")
    public List<Zeitbuchung> getAllZeitbuchungen(){
        return service.getAllZeitbuchungen();
    }

    @GetMapping(value = "/zeitbuchung/{id}")
    public Zeitbuchung getZeitbuchungById(@PathVariable Integer id){
        return service.getZeitbuchungById(id);
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
