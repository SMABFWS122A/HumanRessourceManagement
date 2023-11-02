package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Gleitzeit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GleitzeitRepositoryTest {

    @Autowired
    GleitzeitRepository repository;

    Gleitzeit gleitzeit1 = new Gleitzeit();
    Gleitzeit gleitzeit2 = new Gleitzeit();

    @BeforeAll
    void setUp(){

        gleitzeit1.setGleitzeitsaldo(30);
        gleitzeit1.setDatum(Date.valueOf(LocalDate.now().minusDays(1)));
        gleitzeit1.setPersonalnummer(100);
        repository.saveAndFlush(gleitzeit1);

        gleitzeit2.setGleitzeitsaldo(-10);
        gleitzeit2.setDatum(Date.valueOf(LocalDate.now().minusDays(1)));
        gleitzeit2.setPersonalnummer(100);
        repository.saveAndFlush(gleitzeit2);
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
    void findLatestByPersonalnummer_whenFound_thenReturnLatestEntity(){
        // act
        var actual = repository.findFirstByPersonalnummerOrderByDatumDescZeitstempelDesc(100);
        // assert
        assertThat(actual.getGleitzeitsaldo()).isEqualTo(-10);
        assertThat(actual.getDatum()).isEqualTo(Date.valueOf(LocalDate.now().minusDays(1)));
        assertThat(actual.getPersonalnummer()).isEqualTo(100);
    }


    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }

}
