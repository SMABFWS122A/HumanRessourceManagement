package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FehlermeldungRepository extends JpaRepository<Fehlermeldung, Integer> {
    Optional<Fehlermeldung> findFirstByPersonalnummerOrderByZeitstempelDesc(Integer personalnummer);

}
