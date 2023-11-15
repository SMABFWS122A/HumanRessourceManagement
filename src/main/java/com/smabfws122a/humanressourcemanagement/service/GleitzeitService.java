package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
import com.smabfws122a.humanressourcemanagement.entity.Gleitzeit;
import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.repository.BeschaeftigungsgradRepository;
import com.smabfws122a.humanressourcemanagement.repository.GleitzeitRepository;
import com.smabfws122a.humanressourcemanagement.repository.MitarbeiterRepository;
import com.smabfws122a.humanressourcemanagement.repository.ZeitbuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GleitzeitService {

    @Autowired
    GleitzeitRepository repository;

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    ZeitbuchungRepository zeitbuchungRepository;

    @Autowired
    BeschaeftigungsgradRepository beschaeftigungsgradRepository;

    @Autowired
    UrlaubsbuchungService urlaubsbuchungService;

    @Autowired
    FehlermeldungService fehlermeldungService;

    public Optional<Gleitzeit> getLatestGleitzeitByPersonalnummer(Integer personalnummer) {
        return repository.findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(personalnummer);
    }

    public void addGleitzeit(Gleitzeit gleitzeit){
        repository.save(gleitzeit);
    }

    @Scheduled(cron = "0 0 22 * * *") //Jeden Tag um 22:00 Uhr
    public void addGleitzeitForEachMitarbeiter(){
        var mitarbeiterList = mitarbeiterRepository.findAll();
        for (var mitarbeiter: mitarbeiterList
        ) {
            var gleitzeit = new Gleitzeit();
            gleitzeit.setGleitzeitsaldo(calculateNewGleitzeitsaldo(mitarbeiter));
            gleitzeit.setDatum(Date.valueOf(LocalDate.now().minusDays(1)));
            gleitzeit.setPersonalnummer(mitarbeiter.getPersonalnummer());
            addGleitzeit(gleitzeit);
        }
    }

    private Integer calculateNewGleitzeitsaldo(Mitarbeiter mitarbeiter) {
        var gleitzeitsaldoVortag = repository.findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(mitarbeiter.getPersonalnummer()).get().getGleitzeitsaldo();
        if(urlaubsbuchungService.getUrlaubsbuchungVorhanden(mitarbeiter.getPersonalnummer(), LocalDate.now()))
        {
            return gleitzeitsaldoVortag;
        }
        var zeitbuchungen = zeitbuchungRepository.findAllByPersonalnummerAndDatumOrderByUhrzeitAsc(mitarbeiter.getPersonalnummer(),Date.valueOf(LocalDate.now()));
        Double sollArbeitszeit = (beschaeftigungsgradRepository.findById(mitarbeiter.getBeschaeftigungsgrad_id()).get().getWochenstunden() / 5) * 60;
        if (!checkIfZeitbuchungenAreInRightOrder(zeitbuchungen, mitarbeiter)) return 0;
        var arbeitszeit = calculateArbeitszeitInMinutes(zeitbuchungen);
        var pause = calculatePauseInMinutes(zeitbuchungen);
        var gleitzeitsaldo = calculateGleitzeitsaldoInMinutes(arbeitszeit, pause, sollArbeitszeit.intValue(), mitarbeiter);
        return gleitzeitsaldoVortag + gleitzeitsaldo;
    }

    private boolean checkIfZeitbuchungenAreInRightOrder(List<Zeitbuchung> zeitbuchungen, Mitarbeiter mitarbeiter){
        for (int i = 0; i < zeitbuchungen.size(); i++)
        {
            if(i%2==0)
            {
                if (!zeitbuchungen.get(i).getBuchungsart().equals("kommen"))
                {
                    createExeption("Kommenbuchung fehlt! Bitte Korrigieren!", mitarbeiter);
                    return false;
                }
            }

            if(i%2!=0)
            {
                if (!zeitbuchungen.get(i).getBuchungsart().equals("gehen"))
                {
                    createExeption("Gehenbuchung fehlt! Bitte Korrigieren!", mitarbeiter);
                    return false;
                }
            }
        }
        return true;
    }

    private void createExeption(String fehlermeldungText, Mitarbeiter mitarbeiter) {
        var fehlermeldung = new Fehlermeldung();
        fehlermeldung.setFehlermeldung(fehlermeldungText);
        fehlermeldung.setPersonalnummer(mitarbeiter.getPersonalnummer());
        fehlermeldungService.addFehlermeldung(fehlermeldung);
    }

    private Integer calculateArbeitszeitInMinutes(List<Zeitbuchung> zeitbuchungen){
        long arbeitszeit = 0L;
        for (int i = 1; i < zeitbuchungen.size(); i = i+2) {
            arbeitszeit = arbeitszeit + zeitbuchungen.get(i).getUhrzeit().getTime() - zeitbuchungen.get(i - 1).getUhrzeit().getTime();
        }
        return (int) arbeitszeit / 60000;
    }

    private Integer calculatePauseInMinutes(List<Zeitbuchung> zeitbuchungen){
        long pause = 0L;
        for (int i = 1; i < zeitbuchungen.size() - 1; i = i+2) {
            pause = pause + zeitbuchungen.get(i + 1).getUhrzeit().getTime() - zeitbuchungen.get(i).getUhrzeit().getTime();
        }
        return (int) pause / 60000;
    }

    private Integer calculateGleitzeitsaldoInMinutes(Integer arbeitszeit, Integer pause, Integer sollArbeitszeit, Mitarbeiter mitarbeiter) {
        var gleitzeitsaldo = 0;
        if (arbeitszeit > 360 && arbeitszeit < 390) { //Arbeitszeit liegt zwischen 6:00 und 6:30 Stunden
            gleitzeitsaldo = calculateGLeitzeitsaldoWhenArbeitszeitIsBetween6And6_30Hours(arbeitszeit, pause, sollArbeitszeit);
        } else if ((arbeitszeit >= 390 && arbeitszeit <= 540) && pause < 30) { //Arbeitszeit liegt zwischen 6:30 und 9:00 Stunden + Mehr als 30 Minuten Pause
            gleitzeitsaldo = calculateGleitszeitsaldoWhenArbeitszeitIsBetween6_30And9HoursAndPauseIsHigher30Minutes(arbeitszeit, pause, sollArbeitszeit);
        } else if (arbeitszeit > 540 && arbeitszeit < 555) { //Arbeitszeit liegt zwischen 9:00 und 9:15 Stunden
            gleitzeitsaldo = calculateGleitzeitsaldoWhenArbeitszeitIsBetween9And9_15Hours(arbeitszeit, pause, sollArbeitszeit);
        } else if ((arbeitszeit >= 555) && pause < 45) { //Arbeitszeit liegt über 9:14 Stunden + Mehr als 44 Minuten Pause
            gleitzeitsaldo = calculateGleitzeitsaldoWhenArbeitszeitIsHigher9_14HoursAndIsHigher45Minutes(arbeitszeit, pause, sollArbeitszeit, mitarbeiter);
        } else if (arbeitszeit > 600) { //Arbeitszeit liegt über 10:00 Stunden
            gleitzeitsaldo = calculateGleitzeitsaldoWhenArbeitszeitIsHigher10Hours(sollArbeitszeit, mitarbeiter);
        } else {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit;
            createExeption("Keine Fehler in der Zeitbewertung.", mitarbeiter);
        }
        return gleitzeitsaldo;
    }

    private Integer calculateGleitzeitsaldoWhenArbeitszeitIsHigher10Hours(Integer sollArbeitszeit, Mitarbeiter mitarbeiter) {
        int gleitzeitsaldo;
        Integer arbeitszeit;
        createExeption("Die maximale Arbeitszeit von 10 Stunden wurde überschritten und auf 10 Stunden gekappt.", mitarbeiter);
        arbeitszeit = 600;
        gleitzeitsaldo = arbeitszeit - sollArbeitszeit;
        return gleitzeitsaldo;
    }

    private Integer calculateGleitzeitsaldoWhenArbeitszeitIsHigher9_14HoursAndIsHigher45Minutes(Integer arbeitszeit, Integer pause, Integer sollArbeitszeit, Mitarbeiter mitarbeiter) {
        int gleitzeitsaldo;
        if (arbeitszeit > 600){
            createExeption("Die maximale Arbeitszeit von 10 Stunden wurde überschritten und auf 10 Stunden gekappt.", mitarbeiter);
            arbeitszeit = 600;
        }
        if ((arbeitszeit - 540 + pause) < 45) {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (arbeitszeit - 540 + pause);
        } else {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (45 - pause);
        }
        return gleitzeitsaldo;
    }

    private Integer calculateGleitzeitsaldoWhenArbeitszeitIsBetween9And9_15Hours(Integer arbeitszeit, Integer pause, Integer sollArbeitszeit) {
        int gleitzeitsaldo;
        if (pause <= (45 - (arbeitszeit - 540))) {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (45 - pause);
        } else {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit;
        }
        return gleitzeitsaldo;
    }

    private Integer calculateGleitszeitsaldoWhenArbeitszeitIsBetween6_30And9HoursAndPauseIsHigher30Minutes(Integer arbeitszeit, Integer pause, Integer sollArbeitszeit) {
        int gleitzeitsaldo;
        gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (30 - pause);
        return gleitzeitsaldo;
    }

    private Integer calculateGLeitzeitsaldoWhenArbeitszeitIsBetween6And6_30Hours(Integer arbeitszeit, Integer pause, Integer sollArbeitszeit) {
        int gleitzeitsaldo;
        if ((arbeitszeit - 360 + pause) < 30) {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (arbeitszeit - 360 + pause);
        } else {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (30 - pause);
        }
        return gleitzeitsaldo;
    }

}
