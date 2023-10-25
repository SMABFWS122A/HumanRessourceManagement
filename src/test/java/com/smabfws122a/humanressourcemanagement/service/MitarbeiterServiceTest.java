package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MitarbeiterServiceTest {


    @Autowired
    private MitarbeiterService service;

    private final Mitarbeiter mitarbeiter10 = new Mitarbeiter();
    private final Mitarbeiter mitarbeiter20 = new Mitarbeiter();

    @BeforeAll
    void setUp() {
        mitarbeiter10.setPersonalnummer(10);
        mitarbeiter10.setVorname("Max");
        mitarbeiter10.setNachname("Meier");
        mitarbeiter10.setEmail("maxmeier@email.com");
        mitarbeiter10.setBeschaeftigungsgrad_id(1);

        mitarbeiter20.setPersonalnummer(20);
        mitarbeiter20.setVorname("Maria");
        mitarbeiter20.setNachname("Meier");
        mitarbeiter20.setEmail("mariameier@email.com");
        mitarbeiter20.setBeschaeftigungsgrad_id(2);
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllMitarbeiter_checkNumberOfEntitiesBeforeAddingTestData_mustBe5() {
        //arrange
        var expectedEntities = 5;
        //actual
        var actualEntities = service.getAllMitarbeiter();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addMitarbeiter_whenValidModel_thenReturnEntityId() {
        //actual
        var actualId1 = service.addMitarbeiter(mitarbeiter10);
        var actualId2 = service.addMitarbeiter(mitarbeiter20);
        //assert
        assertThat(actualId1).isEqualTo(10);
        assertThat(actualId2).isEqualTo(20);
    }

    @Order(3)
    @Test
    void getAllMitarbeiter_checkNumberOfEntitiesAfterAddingTestData_mustBe7() {
        //arrange
        var expectedEntities = 7;
        //actual
        var actualEntities = service.getAllMitarbeiter();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getMitarbeiterByPersonalnummer_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = service.getMitarbeiterByPersonalnummer(10);
        //assert
        assertEquals(10, actualEntity.getPersonalnummer());
    }

    @Order(5)
    @Test
    void getMitarbeiterByPersonalnummer_whenEntityNotExists_thenReturnThrowException() {
        //arrange
        String expectedMessage = "Could not find Mitarbeiter with personalnummer = " + 9999;
        //assert
        assertThatThrownBy(() -> service.getMitarbeiterByPersonalnummer(9999)).isInstanceOf(IllegalArgumentException.class).hasMessage(expectedMessage);
    }

    @Order(6)
    @Test
    void updateMitarbeiter_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedMitarbeiter = new Mitarbeiter();
        updatedMitarbeiter.setPersonalnummer(10);
        updatedMitarbeiter.setVorname("Max");
        updatedMitarbeiter.setNachname("Meier");
        updatedMitarbeiter.setEmail("maxmeier@email.com");
        updatedMitarbeiter.setBeschaeftigungsgrad_id(3);
        //actual
        var actualId = service.updateMitarbeiter(updatedMitarbeiter);
        var actualEntity = service.getMitarbeiterByPersonalnummer(actualId);
        //assert
        assertThat(actualId).isEqualTo(10);
        assertThat(actualEntity.getBeschaeftigungsgrad_id()).isEqualTo(3);}

    @Order(7)
    @Test
    void deleteMitarbeiterByPersonalnummer_whenSuccessful_thenSizeMustBe6() {
        //arrange
        var expectedEntities = 6;
        //actual
        service.deleteMitarbeiterByPersonalnummer(20);

        var actualEntities = service.getAllMitarbeiter();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(8)
    @Test
    @Sql(statements = {
            "DELETE FROM mitarbeiter WHERE Personalnummer = 10",
            "DELETE FROM mitarbeiter WHERE Personalnummer = 20"
    })
    void getAllMitarbeiter_checkNumberOfEntitiesAfterDeletingTestData_must5() {
        //arrange
        var expectedEntities = 5;
        //actual
        var actualEntities = service.getAllMitarbeiter();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

}