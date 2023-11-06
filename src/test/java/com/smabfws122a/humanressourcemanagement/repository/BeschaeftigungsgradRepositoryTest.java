package com.smabfws122a.humanressourcemanagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeschaeftigungsgradRepositoryTest {

    @Autowired
    BeschaeftigungsgradRepository repository;

    @Test
    void smokeTest(){ assertThat(repository).isNotNull(); }

    @Test
    void findByID_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findById(1);
        // assert
        assertThat(actual.get().getBezeichnung()).isEqualTo("Vollzeit");
        assertThat(actual.get().getWochenstunden()).isEqualTo(40);
        assertThat(actual.get().getUrlaubsanspruch()).isEqualTo(30);
        assertThat(actual.get().getBeginn_arbeitszeitfenster()).isEqualTo(Time.valueOf("7:00:00"));
        assertThat(actual.get().getEnde_arbeitszeitfenster()).isEqualTo(Time.valueOf("20:00:00"));
    }

}
