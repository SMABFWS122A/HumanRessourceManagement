package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FehlermeldungServiceTest {


    @Autowired
    private FehlermeldungService service;

    private final Fehlermeldung fehlermeldung10 = new Fehlermeldung();
    private final Fehlermeldung fehlermeldung20 = new Fehlermeldung();

    @BeforeAll
    void setUp() {
        fehlermeldung10.setFehlermeldung("FEHLER 10");
        fehlermeldung10.setPersonalnummer(100);


        fehlermeldung20.setFehlermeldung("FEHLER 20");
        fehlermeldung20.setPersonalnummer(200);
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllFehlermeldung_checkNumberOfEntitiesBeforeAddingTestData_mustBe0() {
        //arrange
        var expectedEntities = 0;
        //actual
        var actualEntities = service.getAllFehlermeldung();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addFehlermeldung_whenValidModel_thenReturnEntityId() {
        //actual
        var actualId1 = service.addFehlermeldung(fehlermeldung10);
        var actualId2 = service.addFehlermeldung(fehlermeldung20);
        //assert
        assertThat(actualId1).isEqualTo(100);
        assertThat(actualId2).isEqualTo(200);
    }

    @Order(3)
    @Test
    void getAllFehlermeldung_checkNumberOfEntitiesAfterAddingTestData_mustBe2() {
        //arrange
        var expectedEntities = 2;
        //actual
        var actualEntities = service.getAllFehlermeldung();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getFehlermeldungByEmail_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = service.getLatestFehlermeldungbyPersonalnummer(100);
        //assert
        assertThat(actualEntity).isPresent();
        assertEquals(100, actualEntity.get().getPersonalnummer());
    }

    @Order(5)
    @Test
    void getFehlermeldungByEmail_whenEntityNotExists_thenReturnThrowException() {
        //assert
        assertThat(service.getLatestFehlermeldungbyPersonalnummer(300)).isNotPresent();
    }


    @Order(6)
    @Test
    void updateFehlermeldung_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedFehlermeldung = new Fehlermeldung();
        updatedFehlermeldung.setPersonalnummer(200);
        updatedFehlermeldung.setFehlermeldung("FEHLER UPDATED");
        //actual
        var actualId = service.updateFehlermeldung(updatedFehlermeldung);
        var actualEntity = service.getLatestFehlermeldungbyPersonalnummer(actualId);
        //assert
        assertThat(actualId).isEqualTo(200);
        assertThat(actualEntity).isPresent();
        assertThat(actualEntity.get().getFehlermeldung()).isEqualTo("FEHLER UPDATED");}

    @Order(8)
    @Test
    @Sql(statements = {
            "DELETE FROM fehlermeldung WHERE personalnummer = 100",
            "DELETE FROM fehlermeldung WHERE personalnummer = 200",
    })
    void getAllFehlermeldung_checkNumberOfEntitiesAfterDeletingTestData_must0() {
        //arrange
        var expectedEntities = 0;
        //actual
        var actualEntities = service.getAllFehlermeldung();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

}