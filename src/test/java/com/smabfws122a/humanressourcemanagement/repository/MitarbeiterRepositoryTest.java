package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
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
public class MitarbeiterRepositoryTest {

    @Autowired
    MitarbeiterRepository repository;

    Mitarbeiter mitarbeiterHans = new Mitarbeiter();
    Mitarbeiter mitarbeiterPeter = new Mitarbeiter();

    @BeforeAll
    void setUp(){

        mitarbeiterHans.setPersonalnummer(100);
        mitarbeiterHans.setVorname("Hans");
        mitarbeiterHans.setNachname("Mueller");
        mitarbeiterHans.setBeschaeftigungsgrad_id(1);
        repository.saveAndFlush(mitarbeiterHans);

        mitarbeiterPeter.setPersonalnummer(200);
        mitarbeiterPeter.setVorname("Peter");
        mitarbeiterPeter.setNachname("Zimmermann");
        mitarbeiterPeter.setPersonalnummer(2);
        repository.saveAndFlush(mitarbeiterPeter);
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
    void findById_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findById(100);
        // assert
        assertThat(actual.get().getPersonalnummer()).isEqualTo(100);
        assertThat(actual.get().getVorname()).isEqualTo("Hans");
        assertThat(actual.get().getNachname()).isEqualTo("Mueller");
        assertThat(actual.get().getBeschaeftigungsgrad_id()).isEqualTo(1);
    }

    @Test
    void update_whenSuccesfull_thenEntityHasNewUhrzeit(){
        // arrange
        var updateNachname = new Mitarbeiter();
        updateNachname.setPersonalnummer(100);
        updateNachname.setVorname("Hans");
        updateNachname.setNachname("Schmidt");
        updateNachname.setBeschaeftigungsgrad_id(1);
        // act
        repository.save(updateNachname);
        var actual = repository.findById(100);
        // assert
        assertThat(actual.get().getPersonalnummer()).isEqualTo(100);
        assertThat(actual.get().getVorname()).isEqualTo("Hans");
        assertThat(actual.get().getNachname()).isEqualTo("Schmidt");
        assertThat(actual.get().getBeschaeftigungsgrad_id()).isEqualTo(1);
    }

    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }
}
