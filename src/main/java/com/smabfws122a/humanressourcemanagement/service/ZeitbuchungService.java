package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.repository.ZeitbuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ZeitbuchungService {
    @Autowired
    private ZeitbuchungRepository repository;

    public Integer addZeitbuchung(Zeitbuchung zeitbuchung){
        return repository.save(zeitbuchung).getId();
    }

    public List<Zeitbuchung> getAllZeitbuchungenByPersonalnummerAndDatum(Integer personalnummer, Date datum) {
        return repository.findAllByPersonalnummerAndDatumOrderByUhrzeitAsc(personalnummer, datum);
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
