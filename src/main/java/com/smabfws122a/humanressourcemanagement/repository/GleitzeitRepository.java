package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Gleitzeit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GleitzeitRepository extends JpaRepository<Gleitzeit, Integer> {
    Gleitzeit findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(Integer personalnummer);

}
