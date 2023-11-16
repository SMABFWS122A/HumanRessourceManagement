package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ZeitbuchungServiceTest {

    @Autowired
    private ZeitbuchungService service;

    private final Zeitbuchung zeitbuchungKommen = new Zeitbuchung();
    private final Zeitbuchung zeitbuchungGehen = new Zeitbuchung();

    @BeforeAll
    void setUp(){
        zeitbuchungKommen.setUhrzeit(Time.valueOf("07:00:00"));
        zeitbuchungKommen.setDatum(Date.valueOf("2023-10-23"));
        zeitbuchungKommen.setBuchungsart("kommen");
        zeitbuchungKommen.setPersonalnummer(100);

        zeitbuchungGehen.setUhrzeit(Time.valueOf("15:00:00"));
        zeitbuchungGehen.setDatum(Date.valueOf("2023-10-23"));
        zeitbuchungGehen.setBuchungsart("gehen");
        zeitbuchungGehen.setPersonalnummer(100);
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllZeitbuchungenByPersonalnummerAndDatum_checkNumberOfEntitiesBeforeAddingTestData_mustBeEmpty() {
        //arrange
        var expectedEntities = 0;
        //actual
        var actualEntities = service.getAllZeitbuchungenByPersonalnummerAndDatum(100, Date.valueOf("2023-10-23"));
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addZeitbuchung_whenValidModel_thenReturnEntityId() {
        //actual
        var actualId1 = service.addZeitbuchung(zeitbuchungKommen);
        var actualId2 = service.addZeitbuchung(zeitbuchungGehen);
        //assert
        assertThat(actualId1).isEqualTo(1);
        assertThat(actualId2).isEqualTo(2);
    }

    @Order(3)
    @Test
    void getAllZeitbuchungenByPersonalnummerAndDatum_checkNumberOfEntitiesAfterAddingTestData_mustBe2() {
        //arrange
        var expectedEntities = 2;
        //actual
        var actualEntities = service.getAllZeitbuchungenByPersonalnummerAndDatum(100, Date.valueOf("2023-10-23"));
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getZeitbuchungById_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = service.getZeitbuchungById(1);
        //assert
        assertThat(actualEntity).isPresent();
        assertEquals(1, actualEntity.get().getId());
    }

    @Order(5)
    @Test
    void getZeitbuchungById_whenEntityNotExists_thenReturnThrowException() {
        //assert
        assertThat(service.getZeitbuchungById(999)).isNotPresent();
    }

    @Order(6)
    @Test
    void updateZeitbuchung_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedZeitbuchung = new Zeitbuchung();
        updatedZeitbuchung.setId(1);
        updatedZeitbuchung.setUhrzeit(Time.valueOf("08:00:00"));
        updatedZeitbuchung.setDatum(Date.valueOf("2023-10-23"));
        updatedZeitbuchung.setBuchungsart("kommen");
        updatedZeitbuchung.setPersonalnummer(100);
        //actual
        var actualId = service.updateZeitbuchung(updatedZeitbuchung);
        var actualEntity = service.getZeitbuchungById(actualId);
        //assert
        assertThat(actualId).isEqualTo(1);
        assertThat(actualEntity).isPresent();
        assertThat(actualEntity.get().getUhrzeit()).isEqualTo(Time.valueOf("08:00:00"));
    }

    @Order(7)
    @Test
    void deleteZeitbuchungById_whenSuccessful_thenSizeMustBe1() {
        //arrange
        var expectedEntities = 1;
        //actual
        service.deleteZeitbuchungById(2);

        var actualEntities = service.getAllZeitbuchungenByPersonalnummerAndDatum(100, Date.valueOf("2023-10-23"));
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(8)
    @Test
    @Sql(statements = {
            "DELETE FROM zeitbuchung",
            "ALTER SEQUENCE zeitbuchung_id_seq RESTART;"
    })
    void getAllZeitbuchungenByPersonalnummerAndDatum_checkNumberOfEntitiesAfterDeletingTestData_mustBeEmpty() {
        //arrange
        var expectedEntities = 0;
        //actual
        var actualEntities = service.getAllZeitbuchungenByPersonalnummerAndDatum(100, Date.valueOf("2023-10-23"));
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }
}
