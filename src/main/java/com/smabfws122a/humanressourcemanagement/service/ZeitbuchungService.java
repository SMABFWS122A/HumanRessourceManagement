package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.repository.ZeitbuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZeitbuchungService {
    @Autowired
    private ZeitbuchungRepository repository;

    public Integer addZeitbuchung(Zeitbuchung zeitbuchung){
        return repository.save(zeitbuchung).getId();
    }

    public List<Zeitbuchung> getAllZeitbuchungen() {
        return repository.findAll();
    }

    public Optional<Zeitbuchung> getZeitbuchungById(Integer id) {
        return repository.findById(id);
    }

    public Integer updateZeitbuchung(Zeitbuchung zeitbuchung) {
        return repository.save(zeitbuchung).getId();
    }

    public void deleteZeitbuchungById(Integer id) {
        repository.deleteById(id);
    }
}
