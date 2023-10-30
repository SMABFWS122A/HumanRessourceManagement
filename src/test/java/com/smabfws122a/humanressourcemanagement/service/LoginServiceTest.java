package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Login;
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
public class LoginServiceTest {


    @Autowired
    private LoginService service;

    private final Login login10 = new Login();
    private final Login login20 = new Login();

    @BeforeAll
    void setUp() {
        login10.setEmail("monikaschmitz10@mail.de");
        login10.setPasswort("monikaschmitz");
        login10.setAdmin(false);
        login10.setPersonalnummer(200);


        login20.setEmail("hansmueller.admin10@mail.de");
        login20.setPasswort("root");
        login20.setAdmin(true);
        login20.setPersonalnummer(100);
    }

    @Test
    void smokeTest() {
        assertThat(service).isNotNull();
    }

    @Order(1)
    @Test
    void getAllLogin_checkNumberOfEntitiesBeforeAddingTestData_mustBe6() {
        //arrange
        var expectedEntities = 6;
        //actual
        var actualEntities = service.getAllLogin();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(2)
    @Test
    void addLogin_whenValidModel_thenReturnEntityId() {
        //actual
        var actualId1 = service.addLogin(login10);
        var actualId2 = service.addLogin(login20);
        //assert
        assertThat(actualId1).isEqualTo("monikaschmitz10@mail.de");
        assertThat(actualId2).isEqualTo("hansmueller.admin10@mail.de");
    }

    @Order(3)
    @Test
    void getAllLogin_checkNumberOfEntitiesAfterAddingTestData_mustBe8() {
        //arrange
        var expectedEntities = 8;
        //actual
        var actualEntities = service.getAllLogin();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(4)
    @Test
    void getLoginByEmail_whenEntityExists_thenReturnEntity() {
        //actual
        var actualEntity = service.getLoginbyEmail("monikaschmitz10@mail.de");
        //assert
        assertEquals(200, actualEntity.getPersonalnummer());
    }

    @Order(5)
    @Test
    void getLoginByEmail_whenEntityNotExists_thenReturnThrowException() {
        //arrange
        String expectedMessage = "Could not find Login with email = " + "testmail1@mail.de";
        //assert
        assertThatThrownBy(() -> service.getLoginbyEmail("testmail1@mail.de")).isInstanceOf(IllegalArgumentException.class).hasMessage(expectedMessage);
    }

    @Order(6)
    @Test
    void updateLogin_whenValidModel_thenReturnEntityId() {
        //arrange
        var updatedLogin = new Login();
        updatedLogin.setPersonalnummer(200);
        updatedLogin.setEmail("monikaschmitz10@mail.de");
        updatedLogin.setPasswort("monikaschmitz");
        updatedLogin.setAdmin(true);
        //actual
        var actualId = service.updateLogin(updatedLogin);
        var actualEntity = service.getLoginbyEmail(actualId);
        //assert
        assertThat(actualId).isEqualTo("monikaschmitz10@mail.de");
        assertThat(actualEntity.getAdmin()).isEqualTo(true);}

    @Order(7)
    @Test
    void deleteLoginByEmail_whenSuccessful_thenSizeMustBe7() {
        //arrange
        var expectedEntities = 7;
        //actual
        service.deleteLoginbyEmail("monikaschmitz10@mail.de");

        var actualEntities = service.getAllLogin();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

    @Order(8)
    @Test
    @Sql(statements = {
            "DELETE FROM login WHERE email = 'monikaschmitz10@mail.de'",
            "DELETE FROM login WHERE email = 'hansmueller.admin10@mail.de'"
    })
    void getAllLogin_checkNumberOfEntitiesAfterDeletingTestData_must6() {
        //arrange
        var expectedEntities = 6;
        //actual
        var actualEntities = service.getAllLogin();
        //assert
        assertEquals(expectedEntities, actualEntities.size());
    }

}