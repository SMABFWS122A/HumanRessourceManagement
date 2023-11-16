package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Urlaubsbuchung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface UrlaubsbuchungRepository extends JpaRepository<Urlaubsbuchung, Integer> {
    boolean existsUrlaubsbuchungsByPersonalnummerEqualsAndVonDatumIsBeforeAndBisDatumIsAfter(Integer personalnummer, Date vonDatum, Date bisDatum);
    List<Urlaubsbuchung> findAllByPersonalnummerEqualsAndVonDatumIsBetweenOrPersonalnummerEqualsAndBisDatumIsBetween(Integer personalnummer1, Date monatsAnfang1, Date monatsEnde1, Integer personalnummer2, Date monatsAnfang2, Date monatsEnde2);
}
