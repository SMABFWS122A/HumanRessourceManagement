package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.entity.Urlaubsbuchung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UrlaubsbuchungServiceTest {


    @Autowired
    private UrlaubsbuchungService service;

    @Autowired
    MitarbeiterService mitarbeiterService;

    private final Urlaubsbuchung urlaubsbuchung10 = new Urlaubsbuchung();
    private final Urlaubsbuchung urlaubsbuchung20 = new Urlaubsbuchung();
    private final Mitarbeiter mitarbeiter10 = new Mitarbeiter();


    @BeforeAll
    void setUp() {
        mitarbeiter10.setPersonalnummer(-1);
        mitarbeiter10.setVorname("Max");
        mitarbeiter10.setNachname("Meier");
        mitarbeiter10.setEmail("maxmeier@email.com");
        mitarbeiter10.setBeschaeftigungsgrad_id(1);

        urlaubsbuchung10.setVonDatum(Date.valueOf(LocalDate.now().minusDays(2)));
        urlaubsbuchung10.setBisDatum(Date.valueOf(LocalDate.now().plusDays(4)));
        urlaubsbuchung10.setBuchungsart("Tarifurlaub");
        urlaubsbuchung10.setPersonalnummer(100);

        urlaubsbuchung20.setVonDatum(Date.valueOf(LocalDate.now()));
        urlaubsbuchung20.setBisDatum(Date.valueOf(LocalDate.now()));
        urlaubsbuchung20.setBuchungsart("Tarifurlaub");
        urlaubsbuchung20.setPersonalnummer(-1);
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllUrlaubsbuchungen_checkNumberOfEntitiesBeforeAddingTestData_mustBe6() {
        //arrange
        var expectedEntities = 6;
        //actual
        var actualEntities = service.getAllUrlaubsbuchungen();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addUrlaubsbuchung_whenValidModel_thenReturnEntityId() {
        //actual
        var actualId0 = mitarbeiterService.addMitarbeiter(mitarbeiter10);
        var actualId1 = service.addUrlaubsbuchung(urlaubsbuchung10);
        var actualId2 = service.addUrlaubsbuchung(urlaubsbuchung20);
        //assert
        assertThat(actualId1).isEqualTo(7);
        assertThat(actualId2).isEqualTo(8);
    }

    @Order(3)
    @Test
    void getAllUrlaubsbuchungen_checkNumberOfEntitiesBeforeAddingTestData_mustBe8() {
        //arrange
        var expectedEntities = 8;
        //actual
        var actualEntities = service.getAllUrlaubsbuchungen();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getUrlaubsbuchungById_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = service.getUrlaubsbuchungById(7);
        //assert
        assertThat(actualEntity).isPresent();
        assertEquals(100, actualEntity.get().getPersonalnummer());
    }

    @Order(5)
    @Test
    void getUrlaubsbuchungById_whenEntityNotExists_thenReturnThrowException() {
        //assert
        assertThat(service.getUrlaubsbuchungById(-1)).isNotPresent();
    }

    @Order(6)
    @Test
    void updateUrlaubsbuchung_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedUrlaubsbuchung = new Urlaubsbuchung();
        updatedUrlaubsbuchung.setId(7);
        updatedUrlaubsbuchung.setVonDatum(Date.valueOf(LocalDate.now()));
        updatedUrlaubsbuchung.setBisDatum(Date.valueOf(LocalDate.now().plusDays(4)));
        updatedUrlaubsbuchung.setBuchungsart("Tarifurlaub");
        updatedUrlaubsbuchung.setPersonalnummer(100);
        //actual
        var actualId = service.updateUrlaubsbuchung(updatedUrlaubsbuchung);
        var actualEntity = service.getUrlaubsbuchungById(actualId);
        //assert
        assertThat(actualId).isEqualTo(7);
        assertThat(actualEntity).isPresent();
        assertThat(actualEntity.get().getVonDatum()).isEqualTo(Date.valueOf(LocalDate.now()));
    }

    @Order(7)
    @Test
    void getUrlaubsbuchungVorhanden_whenFound_thenReturnTru() {
        //actual
        var actual = service.getUrlaubsbuchungVorhanden(100, LocalDate.now());
        //assert
        assertThat(actual).isTrue();
    }

    @Order(8)
    @Test
    void getUrlaubstageByPersonalnummerAndMonatAndJahr_whenFound_thenReturnListofDates() {
        //actual
        var actual = service.getUrlaubstageByPersonalnummerAndMonatAndJahr(-1,LocalDate.now().with(firstDayOfMonth()));

        //assert
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);
    }

    @Order(9)
    @Test
    void getAnzahlVerfügbareUrlaubstage_whenFound_thenReturnInteger() {
        //actual
        var actual = service.getAnzahlVerfügbareUrlaubstage(-1,LocalDate.now().with(firstDayOfMonth()));

        //assert
        assertThat(actual).isEqualTo(29);

    }

    @Order(10)
    @Test
    @Sql(statements = {
            "DELETE FROM urlaubsbuchung WHERE id = 7",
            "DELETE FROM urlaubsbuchung WHERE id = 8",
            "DELETE FROM mitarbeiter WHERE personalnummer = 10"
    })
    void getAllLogin_checkNumberOfEntitiesAfterDeletingTestData_must6() {
        //arrange
        var expectedEntities = 6;
        //actual
        var actualEntities = service.getAllUrlaubsbuchungen();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

}