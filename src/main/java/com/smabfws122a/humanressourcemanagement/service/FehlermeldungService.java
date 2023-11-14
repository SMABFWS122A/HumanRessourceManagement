package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
import com.smabfws122a.humanressourcemanagement.repository.FehlermeldungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FehlermeldungService {
    @Autowired
    FehlermeldungRepository repository;

    public Integer addFehlermeldung(Fehlermeldung fehlermeldung) {
        return repository.save(fehlermeldung).getPersonalnummer();
    }

    public List<Fehlermeldung> getAllFehlermeldung() {
        return repository.findAll();
    }

    public Optional<Fehlermeldung> getLatestFehlermeldungbyPersonalnummer(Integer personalnummer) {
        return repository.findFirstByPersonalnummerOrderByZeitstempelDesc(personalnummer);
    }

    public Integer updateFehlermeldung(Fehlermeldung fehlermeldung) {
        return repository.save(fehlermeldung).getPersonalnummer();
    }

    public void deleteFehlermeldungbyPersonalnummer(Integer personalnummer) {
        repository.deleteById(personalnummer);
    }

}
