package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Gleitzeit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GleitzeitRepository extends JpaRepository<Gleitzeit, Integer> {
    Optional<Gleitzeit> findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(Integer personalnummer);

}
