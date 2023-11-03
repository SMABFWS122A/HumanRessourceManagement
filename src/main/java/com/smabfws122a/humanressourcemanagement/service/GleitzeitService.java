package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Gleitzeit;
import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import com.smabfws122a.humanressourcemanagement.repository.BeschaeftigungsgradRepository;
import com.smabfws122a.humanressourcemanagement.repository.GleitzeitRepository;
import com.smabfws122a.humanressourcemanagement.repository.MitarbeiterRepository;
import com.smabfws122a.humanressourcemanagement.repository.ZeitbuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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

    public Gleitzeit getLatestGleitzeitByPersonalnummer(Integer personalnummer) {
        addGleitzeitForEachMitarbeiter();
        return repository.findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(personalnummer);
    }

    private void addGleitzeit(Gleitzeit gleitzeit){
        repository.save(gleitzeit);
    }

    //@Scheduled(cron = "0 0 22 * * *") //Jeden Tag um 22:00 Uhr
    //@Scheduled(cron = "0 0 * * * *")  //Zu Beginn jeder Stunde
    //@Scheduled(cron = "*/30 * * * * *") //Alle 30 Sekunden
    private void addGleitzeitForEachMitarbeiter(){
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
        var datumVomVortag = Date.valueOf(LocalDate.now().minusDays(1));
        var zeitbuchungen = zeitbuchungRepository.findAllByPersonalnummerAndDatum(mitarbeiter.getPersonalnummer(), datumVomVortag);
        Double sollArbeitszeit = (beschaeftigungsgradRepository.findById(mitarbeiter.getBeschaeftigungsgrad_id()).get().getWochenstunden() / 5) * 60;
        var gleitzeitsaldoVortag = repository.findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(mitarbeiter.getPersonalnummer()).getGleitzeitsaldo();
        if (!checkIfZeitbuchungenAreInRightOrder(zeitbuchungen)) return 0;
        var arbeitszeit = calculateArbeitszeitInMinutes(zeitbuchungen);
        var pause = calculatePauseInMinutes(zeitbuchungen);
        var gleitzeitsaldo = calculateGleitzeitsaldoInMinutes(arbeitszeit, pause, sollArbeitszeit.intValue());
        return gleitzeitsaldoVortag + gleitzeitsaldo;
    }

    private boolean checkIfZeitbuchungenAreInRightOrder(List<Zeitbuchung> zeitbuchungen){
        for (int i = 0; i < zeitbuchungen.size(); i++)
        {
            if(i%2==0)
            {
                if (!zeitbuchungen.get(i).getBuchungsart().equals("kommen"))
                {
                    //Fehelermeldung schmeißen
                    return false;
                }
            }

            if(i%2!=0)
            {
                if (!zeitbuchungen.get(i).getBuchungsart().equals("gehen"))
                {
                    //Fehelermeldung schmeißen
                    return false;
                }
            }
        }
        return true;
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

    private Integer calculateGleitzeitsaldoInMinutes(Integer arbeitszeit, Integer pause, Integer sollArbeitszeit) {
        var gleitzeitsaldo = 0;
        if (arbeitszeit > 360 && arbeitszeit < 390) {
            if (pause <= (arbeitszeit - 360)) {
                gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (( arbeitszeit - 360) - pause);
            } else {
                gleitzeitsaldo = arbeitszeit - sollArbeitszeit - ((arbeitszeit - 360));
            }
        } else if ((arbeitszeit >= 390 && arbeitszeit <= 540) && pause < 30) {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (30 - pause);
        } else if (arbeitszeit > 540 && arbeitszeit < 555) {
            if (pause <= (30 + (arbeitszeit - 540))) {
                gleitzeitsaldo = arbeitszeit - sollArbeitszeit - ((30 + (arbeitszeit - 540)) - pause);
            } else {
                gleitzeitsaldo = arbeitszeit - sollArbeitszeit - ((arbeitszeit - 540));
            }
        } else if ((arbeitszeit >= 555) && pause < 45) {
            if (arbeitszeit > 600){
                //Fehlermeldung ausgeben
                arbeitszeit = 600;
            }
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit - (45 - pause);
        } else if ((arbeitszeit < 600) && pause >= 45) {
            //Fehlermeldung ausgeben
            arbeitszeit = 600;
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit;
        } else {
            gleitzeitsaldo = arbeitszeit - sollArbeitszeit;
        }
        return gleitzeitsaldo;
    }

}