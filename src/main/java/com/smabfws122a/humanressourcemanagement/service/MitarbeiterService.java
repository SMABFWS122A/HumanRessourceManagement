package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MitarbeiterService {
    @Autowired
     MitarbeiterRepository repository;

    public Integer addMitarbeiter(Mitarbeiter mitarbeiter){
        return repository.save(mitarbeiter).getPersonalnummer();
    }

    public List<Mitarbeiter> getAllMitarbeiter() {
        return repository.findAll();
    }

    public Mitarbeiter getMitarbeiterByPersonalnummer(Integer personalnummer) {
        return repository.findById(personalnummer).orElseThrow(
                () -> new IllegalArgumentException("Could not find Mitarbeiter with personalnummer = " + personalnummer)
        );
    }

    public Integer updateMitarbeiter(Mitarbeiter mitarbeiter) {
        return repository.save(mitarbeiter).getPersonalnummer();
    }

    public void deleteMitarbeiterByPersonalnummer(Integer personalnummer) {
        repository.deleteById(personalnummer);
    }

}
