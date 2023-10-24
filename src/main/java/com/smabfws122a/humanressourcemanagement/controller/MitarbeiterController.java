package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
    @RestController
    public class MitarbeiterController {

        @Autowired
        MitarbeiterService service;

        @PostMapping(value = "/mitarbeiter")
        public Integer addMitarbeiter(@RequestBody Mitarbeiter mitarbeiter){
            return service.addMitarbeiter(mitarbeiter);
        }

        @GetMapping(value = "/mitarbeiter")
        public List<Mitarbeiter> getAllMitarbeiter(){
            return service.getAllMitarbeiter();
        }

        @GetMapping(value = "/mitarbeiter/{personalnummer}")
        public Mitarbeiter getMitarbeiterByPersonalnummer(@PathVariable Integer personalnummer){
            return service.getMitarbeiterByPersonalnummer(personalnummer);
        }

        @PutMapping(value = "/mitarbeiter")
        public Integer updateMitarbeiter(@RequestBody Mitarbeiter mitarbeiter){
            return service.updateMitarbeiter(mitarbeiter);
        }

        @DeleteMapping(value = "/mitarbeiter/{personalnummer}")
        public void deleteMitarbeiterByPersonalnummer(@PathVariable Integer personalnummer){
            service.deleteMitarbeiterByPersonalnummer(personalnummer);
        }
    }

