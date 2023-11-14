package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FehlermeldungRepositoryTest {

    @Autowired
    FehlermeldungRepository repository;

    Fehlermeldung fehlermeldung1 = new Fehlermeldung();
    Fehlermeldung fehlermeldung2 = new Fehlermeldung();

    @BeforeAll
    void setUp(){

        fehlermeldung1.setFehlermeldung("FEHLER 1");
        fehlermeldung1.setPersonalnummer(100);
        repository.saveAndFlush(fehlermeldung1);

        fehlermeldung2.setFehlermeldung("FEHLER 2");
        fehlermeldung2.setPersonalnummer(200);
        repository.saveAndFlush(fehlermeldung2);
    }

    @Test
    void smokeTest(){ assertThat(repository).isNotNull(); }

    @Test
    void findAll_whenFindAll_thenResultHasSize2(){
        // arrange
        var expectedSize = 2;
        // act
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    void findLatestFehlermeldungById_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findFirstByPersonalnummerOrderByZeitstempelDesc(100);
        // assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getFehlermeldung()).isEqualTo("FEHLER 1");
        assertThat(actual.get().getPersonalnummer()).isEqualTo(100);
    }


    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }

}
