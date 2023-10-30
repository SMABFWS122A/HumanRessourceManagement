package com.smabfws122a.humanressourcemanagement.repository;

import com.smabfws122a.humanressourcemanagement.entity.Login;
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
public class LoginRepositoryTest {

    @Autowired
    LoginRepository repository;

    Login login1 = new Login();
    Login login2 = new Login();

    @BeforeAll
    void setUp(){

        login1.setEmail("testmail1@mail.de");
        login1.setPasswort("StarkesPasswort1");
        login1.setAdmin(false);
        login1.setPersonalnummer(1000);
        repository.saveAndFlush(login1);

        login2.setEmail("testmail2@mail.de");
        login2.setPasswort("StarkesPasswort2");
        login2.setAdmin(true);
        login2.setPersonalnummer(2000);
        repository.saveAndFlush(login2);
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
    void findByID_whenFound_thenReturnEntity(){
        // act
        var actual = repository.findById("testmail1@mail.de");
        // assert
        assertThat(actual.get().getPersonalnummer()).isEqualTo(1000);
        assertThat(actual.get().getEmail()).isEqualTo("testmail1@mail.de");
        assertThat(actual.get().getAdmin()).isEqualTo(false);
        assertThat(actual.get().getPasswort()).isEqualTo("StarkesPasswort1");
    }

    @Test
    void update_whenSuccesfull_thenEntityIsNowAdmin(){
        // arrange
        var updateAdmin = new Login();
        updateAdmin.setPersonalnummer(1000);
        updateAdmin.setPasswort("StarkesPasswort1");
        updateAdmin.setEmail("testmail1@mail.de");
        updateAdmin.setAdmin(true);
        // act
        repository.save(updateAdmin);
        var actual = repository.findById("testmail1@mail.de");
        // assert
        assertThat(actual.get().getPersonalnummer()).isEqualTo(1000);
        assertThat(actual.get().getEmail()).isEqualTo("testmail1@mail.de");
        assertThat(actual.get().getPasswort()).isEqualTo("StarkesPasswort1");
        assertThat(actual.get().getAdmin()).isEqualTo(true);
    }

    @AfterAll
    void tearTown(){
        repository.deleteAll();
    }
}
