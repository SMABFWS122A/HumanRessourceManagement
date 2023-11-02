package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("jpa-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ZeitbuchungRepositoryTest {

    @Autowired
    ZeitbuchungRepository repository;

    Zeitbuchung zeitbuchungKommen = new Zeitbuchung();
    Zeitbuchung zeitbuchungGehen = new Zeitbuchung();
    Zeitbuchung zeitbuchungKommenForOtherPersonalnummer = new Zeitbuchung();


    @BeforeAll
    void setUp(){

        zeitbuchungKommen.setUhrzeit(Time.valueOf("14:52:00"));
        zeitbuchungKommen.setDatum(Date.valueOf("2023-10-23"));
        zeitbuchungKommen.setBuchungsart("kommen");
        zeitbuchungKommen.setPersonalnummer(100);
        repository.saveAndFlush(zeitbuchungKommen);

        zeitbuchungGehen.setUhrzeit(Time.valueOf("19:52:00"));
        zeitbuchungGehen.setDatum(Date.valueOf("2023-10-23"));
        zeitbuchungGehen.setBuchungsart("gehen");
        zeitbuchungGehen.setPersonalnummer(100);
        repository.saveAndFlush(zeitbuchungGehen);

        zeitbuchungKommenForOtherPersonalnummer.setUhrzeit(Time.valueOf("19:52:00"));
        zeitbuchungKommenForOtherPersonalnummer.setDatum(Date.valueOf("2023-10-23"));
        zeitbuchungKommenForOtherPersonalnummer.setBuchungsart("gehen");
        zeitbuchungKommenForOtherPersonalnummer.setPersonalnummer(200);
        repository.saveAndFlush(zeitbuchungKommenForOtherPersonalnummer);
    }

    @Test
    void smokeTest(){ assertThat(repository).isNotNull(); }

    @Test
    void findAll_whenFindAll_thenResultHasSize3(){
        // arrange
        var expectedSize = 3;
        // act
        var actual = repository.findAll();
        // assert
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    void findById_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findById(1);
        // assert
        assertThat(actual.get().getId()).isEqualTo(1);
        assertThat(actual.get().getUhrzeit()).isEqualTo(Time.valueOf("14:52:00"));
        assertThat(actual.get().getDatum()).isEqualTo(Date.valueOf("2023-10-23"));
        assertThat(actual.get().getBuchungsart()).isEqualTo("kommen");
        assertThat(actual.get().getPersonalnummer()).isEqualTo(100);
    }

    @Test
    void findAllByPersonalnummerAndDatum_whenFound_thenReturnEntitiesWithPersonalnummer100AndSizeShouldBe2(){
        // act
        var actualEntities = repository.findAllByPersonalnummerAndDatum(100, Date.valueOf("2023-10-23"));
        // assert
        for (var actualEntity: actualEntities
             ) {
            assertThat(actualEntity.getPersonalnummer()).isEqualTo(100);
        }
        assertThat(actualEntities).hasSize(2);

    }

    @Test
    void update_whenSuccesfull_thenEntityHasNewUhrzeit(){
        // arrange
        var zeitbuchungUpdate = new Zeitbuchung();
        zeitbuchungUpdate.setId(1);
        zeitbuchungUpdate.setUhrzeit(Time.valueOf("15:52:00"));
        zeitbuchungUpdate.setDatum(Date.valueOf("2023-10-23"));
        zeitbuchungUpdate.setBuchungsart("kommen");
        zeitbuchungUpdate.setPersonalnummer(100);
        // act
        repository.save(zeitbuchungUpdate);
        var actual = repository.findById(1);
        // assert
        assertThat(actual.get().getId()).isEqualTo(1);
        assertThat(actual.get().getUhrzeit()).isEqualTo(Time.valueOf("15:52:00"));
        assertThat(actual.get().getDatum()).isEqualTo(Date.valueOf("2023-10-23"));
        assertThat(actual.get().getBuchungsart()).isEqualTo("kommen");
        assertThat(actual.get().getPersonalnummer()).isEqualTo(100);
    }

    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }

}
