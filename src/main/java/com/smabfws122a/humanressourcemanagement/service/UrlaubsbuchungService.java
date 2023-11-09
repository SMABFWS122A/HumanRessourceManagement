package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Urlaubsbuchung;
import com.smabfws122a.humanressourcemanagement.repository.BeschaeftigungsgradRepository;
import com.smabfws122a.humanressourcemanagement.repository.MitarbeiterRepository;
import com.smabfws122a.humanressourcemanagement.repository.UrlaubsbuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UrlaubsbuchungService {
    @Autowired
    UrlaubsbuchungRepository repository;

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    BeschaeftigungsgradRepository beschaeftigungsgradRepository;

    public List<Urlaubsbuchung> getAllUrlaubsbuchungen() {
        return repository.findAll();
    }

    public Integer addUrlaubsbuchung(Urlaubsbuchung urlaubsbuchung){
        return repository.save(urlaubsbuchung).getId();
    }

    public Optional<Urlaubsbuchung> getUrlaubsbuchungById(Integer id) {
        return repository.findById(id);
    }

    public Integer updateUrlaubsbuchung(Urlaubsbuchung urlaubsbuchung) {
        return repository.save(urlaubsbuchung).getId();
    }

    public void deleteUrlaubsbuchungById(Integer id) {
        repository.deleteById(id);
    }


    public boolean getUrlaubsbuchungVorhanden(Integer personalnummer, LocalDate datum){
        return repository.existsUrlaubsbuchungsByPersonalnummerEqualsAndVonDatumIsBeforeAndBisDatumIsAfter(
                personalnummer,
                Date.valueOf(datum.plusDays(1)),
                Date.valueOf(datum.minusDays(1)));
    }
    public List<LocalDate> getUrlaubstageByPersonalnummerAndMonatAndJahr(Integer personalnummer, LocalDate datum){

        List<Urlaubsbuchung> urlaubsbuchungen = repository.findAllByPersonalnummerEqualsAndVonDatumIsBetweenOrPersonalnummerEqualsAndBisDatumIsBetween(
                                personalnummer,
                                Date.valueOf(datum.minusDays(1)),
                                Date.valueOf(datum.plusMonths(1)),
                                personalnummer,
                                Date.valueOf(datum.minusDays(1)),
                                Date.valueOf(datum.plusMonths(1)));

        List<LocalDate> urlaubstageImMonat = new ArrayList<>();
        List<LocalDate> urlaubstage = new ArrayList<>();

        for (var urlaubsbuchung: urlaubsbuchungen
             ) {
                urlaubstage = urlaubsbuchung.getVonDatum().toLocalDate()
                                .datesUntil(urlaubsbuchung.getBisDatum().toLocalDate().plusDays(1))
                                .toList();
            for (var urlaubstag: urlaubstage
                 ) {
                if (urlaubstag.getMonthValue() == datum.getMonthValue()){
                    urlaubstageImMonat.add(urlaubstag);
                }
            }
        }
        return urlaubstageImMonat;
    }

    public Integer getAnzahlVerfügbareUrlaubstage(Integer personalnummer, LocalDate datum){
        List<Urlaubsbuchung> urlaubsbuchungen = repository.findAllByPersonalnummerEqualsAndVonDatumIsBetweenOrPersonalnummerEqualsAndBisDatumIsBetween(
                personalnummer,
                Date.valueOf(datum.minusDays(1)),
                Date.valueOf(datum.plusYears(1)),
                personalnummer,
                Date.valueOf(datum.minusDays(1)),
                Date.valueOf(datum.plusYears(1)));

        List<LocalDate> urlaubstageImJahr = new ArrayList<>();
        List<LocalDate> urlaubstage = new ArrayList<>();

        for (var urlaubsbuchung: urlaubsbuchungen
        ) {
            urlaubstage = urlaubsbuchung.getVonDatum().toLocalDate()
                    .datesUntil(urlaubsbuchung.getBisDatum().toLocalDate().plusDays(1))
                    .toList();
            for (var urlaubstag: urlaubstage
            ) {
                if (urlaubstag.getYear() == datum.getYear()){
                    urlaubstageImJahr.add(urlaubstag);
                }
            }
        }

        return beschaeftigungsgradRepository.
                findById(mitarbeiterRepository.findById(personalnummer)
                        .get().getBeschaeftigungsgrad_id())
                .get().getUrlaubsanspruch()
                - urlaubstageImJahr.size();
    }
}
