package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Mitarbeiter> getMitarbeiterByPersonalnummer(Integer personalnummer) {
        return repository.findById(personalnummer);
    }

    public Integer updateMitarbeiter(Mitarbeiter mitarbeiter) {
        return repository.save(mitarbeiter).getPersonalnummer();
    }

    public void deleteMitarbeiterByPersonalnummer(Integer personalnummer) {
        repository.deleteById(personalnummer);
    }

}
