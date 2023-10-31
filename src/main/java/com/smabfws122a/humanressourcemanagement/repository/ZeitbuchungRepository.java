package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface ZeitbuchungRepository extends JpaRepository<Zeitbuchung, Integer> {
    List<Zeitbuchung> findAllByPersonalnummerAndDatum(Integer personalnumemr, Date datum);
}
