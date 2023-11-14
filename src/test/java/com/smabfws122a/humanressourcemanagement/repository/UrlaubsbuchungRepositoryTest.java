package com.smabfws122a.humanressourcemanagement.repository;


import com.smabfws122a.humanressourcemanagement.entity.Urlaubsbuchung;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UrlaubsbuchungRepositoryTest {

    @Autowired
    UrlaubsbuchungRepository repository;

    Urlaubsbuchung urlaubsbuchung1 = new Urlaubsbuchung();
    Urlaubsbuchung urlaubsbuchung2 = new Urlaubsbuchung();

    @BeforeAll
    void setUp(){
        urlaubsbuchung1.setVonDatum(Date.valueOf(LocalDate.now().minusDays(2)));
        urlaubsbuchung1.setBisDatum(Date.valueOf(LocalDate.now().plusDays(4)));
        urlaubsbuchung1.setBuchungsart("Tarifurlaub");
        urlaubsbuchung1.setPersonalnummer(100);
        repository.saveAndFlush(urlaubsbuchung1);

        urlaubsbuchung2.setVonDatum(Date.valueOf(LocalDate.now()));
        urlaubsbuchung2.setBisDatum(Date.valueOf(LocalDate.now()));
        urlaubsbuchung2.setBuchungsart("Tarifurlaub");
        urlaubsbuchung2.setPersonalnummer(100);
        repository.saveAndFlush(urlaubsbuchung2);
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
    void existsUrlaubsbuchungsByPersonalnummerEqualsAndVonDatumIsBeforeAndBisDatumIsAfter_whenFound_thenReturnTrue(){
        // act
        var actual = repository.existsUrlaubsbuchungsByPersonalnummerEqualsAndVonDatumIsBeforeAndBisDatumIsAfter(100, Date.valueOf(LocalDate.now().plusDays(1)), Date.valueOf(LocalDate.now().minusDays(1)));
        // assert
        assertThat(actual).isTrue();
    }

    @Test
    void findAllByPersonalnummerEqualsAndVonDatumIsBetweenOrPersonalnummerEqualsAndBisDatumIsBetween_whenFound_thenReturnListofUrlaubsbuchungen(){
        // act
       var actual = repository.findAllByPersonalnummerEqualsAndVonDatumIsBetweenOrPersonalnummerEqualsAndBisDatumIsBetween(100, Date.valueOf(LocalDate.now().with(firstDayOfMonth()).minusDays(1)), Date.valueOf(LocalDate.now().with(firstDayOfMonth()).plusMonths(1)), 100, Date.valueOf(LocalDate.now().with(firstDayOfMonth()).minusDays(1)), Date.valueOf(LocalDate.now().with(firstDayOfMonth()).plusMonths(1)));
        // assert
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(2);
        assertThat(actual.stream().findFirst().get().getVonDatum()).isEqualTo(Date.valueOf(LocalDate.now().minusDays(2)));
    }

    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }

}

