package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.repository.ZeitbuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZeitbuchungService {
    @Autowired
    ZeitbuchungRepository repository;

    public Integer addZeitbuchung(Zeitbuchung zeitbuchung){
        return repository.save(zeitbuchung).getId();
    }

    public List<Zeitbuchung> getAllZeitbuchungen() {
        return repository.findAll();
    }

    public Zeitbuchung getZeitbuchungById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Could not find Zeitbuchung with id = " + id)
        );
    }

    public Integer updateZeitbuchung(Zeitbuchung zeitbuchung) {
        return repository.save(zeitbuchung).getId();
    }

    public void deleteZeitbuchungById(Integer id) {
        repository.deleteById(id);
    }
}
