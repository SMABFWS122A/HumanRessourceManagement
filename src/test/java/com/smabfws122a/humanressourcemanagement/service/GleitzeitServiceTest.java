package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GleitzeitServiceTest {

//Vor dem Ausfüheren der Tests muss sichergestellt werden, dass die Methode:
//'addGleitzeitForEachMitarbeiter();' im GleitzeitService nicht auskommentiert wurde.

    @Autowired
    private GleitzeitService service;

    @Autowired
    ZeitbuchungService zeitbuchungService;

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden_thenGleitzeitsaldoForThisDayShouldBeMinus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit5Stunden_thenGleitzeitsaldoForThisDayShouldBeMinus180(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("12:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-180);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden10MinutenWithoutPause_thenGleitzeitsaldoForThisDayShouldBeMinus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:10:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden10MinutenWith10MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:10:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:10:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden10MinutenWith20MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus130(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:20:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:10:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-130);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden30MinutenWithoutPause_thenGleitzeitsaldoForThisDayShouldBeMinus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden30MinutenWith30MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden30MinutenWith31MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus121(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:31:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-121);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden31MinutenWithoutPause_thenGleitzeitsaldoForThisDayShouldBeMinus119(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:31:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-119);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden31MinutenWith30MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus119(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:31:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-119);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit6Stunden31MinutenWith20MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus119(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:20:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("13:31:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-119);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8StundenWithoutPause_thenGleitzeitsaldoForThisDayShouldBeMinus30(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-30);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8StundenWith30MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus30(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-30);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith30MinutenPause_thenGleitzeitsaldoForThisDayShouldBe0(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(0);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith3Mal10MinutenPause_thenGleitzeitsaldoForThisDayShouldBe0(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause1 = new Zeitbuchung();
        gehenPause1.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause1.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause1.setBuchungsart("gehen");
        gehenPause1.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause1);

        var kommenPause1 = new Zeitbuchung();
        kommenPause1.setUhrzeit(Time.valueOf("12:10:00"));
        kommenPause1.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause1.setBuchungsart("kommen");
        kommenPause1.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause1);

        var gehenPause2 = new Zeitbuchung();
        gehenPause2.setUhrzeit(Time.valueOf("12:20:00"));
        gehenPause2.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause2.setBuchungsart("gehen");
        gehenPause2.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause2);

        var kommenPause2 = new Zeitbuchung();
        kommenPause2.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause2.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause2.setBuchungsart("kommen");
        kommenPause2.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause2);

        var gehenPause3 = new Zeitbuchung();
        gehenPause3.setUhrzeit(Time.valueOf("12:40:00"));
        gehenPause3.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause3.setBuchungsart("gehen");
        gehenPause3.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause3);

        var kommenPause3 = new Zeitbuchung();
        kommenPause3.setUhrzeit(Time.valueOf("12:50:00"));
        kommenPause3.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause3.setBuchungsart("kommen");
        kommenPause3.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause3);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(0);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith20MinutenPause_thenGleitzeitsaldoForThisDayShouldBe0(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:20:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(0);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith40MinutenPause_thenGleitzeitsaldoForThisDayShouldBeMinus10(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:40:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-10);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith4Mal10MinutenPause_thenGleitzeitsaldoForThisDayShouldMinus10(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause1 = new Zeitbuchung();
        gehenPause1.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause1.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause1.setBuchungsart("gehen");
        gehenPause1.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause1);

        var kommenPause1 = new Zeitbuchung();
        kommenPause1.setUhrzeit(Time.valueOf("12:10:00"));
        kommenPause1.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause1.setBuchungsart("kommen");
        kommenPause1.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause1);

        var gehenPause2 = new Zeitbuchung();
        gehenPause2.setUhrzeit(Time.valueOf("12:20:00"));
        gehenPause2.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause2.setBuchungsart("gehen");
        gehenPause2.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause2);

        var kommenPause2 = new Zeitbuchung();
        kommenPause2.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause2.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause2.setBuchungsart("kommen");
        kommenPause2.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause2);

        var gehenPause3 = new Zeitbuchung();
        gehenPause3.setUhrzeit(Time.valueOf("12:40:00"));
        gehenPause3.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause3.setBuchungsart("gehen");
        gehenPause3.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause3);

        var kommenPause3 = new Zeitbuchung();
        kommenPause3.setUhrzeit(Time.valueOf("12:50:00"));
        kommenPause3.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause3.setBuchungsart("kommen");
        kommenPause3.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause3);

        var gehenPause4 = new Zeitbuchung();
        gehenPause4.setUhrzeit(Time.valueOf("13:00:00"));
        gehenPause4.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause4.setBuchungsart("gehen");
        gehenPause4.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause4);

        var kommenPause4 = new Zeitbuchung();
        kommenPause4.setUhrzeit(Time.valueOf("13:10:00"));
        kommenPause4.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause4.setBuchungsart("kommen");
        kommenPause4.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause4);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(-10);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9StundenWithoutPause_thenGleitzeitsaldoForThisDayShouldBePlus30(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(30);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden30MinutenWithoutPause_thenGleitzeitsaldoForThisDayShouldBePlus60(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(60);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden30MinutenWith30minutenPause_thenGleitzeitsaldoForThisDayShouldBePlus60(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(60);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden45MinutenWith30minutenPause_thenGleitzeitsaldoForThisDayShouldBePlus60(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:45:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(60);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden46MinutenWith30minutenPause_thenGleitzeitsaldoForThisDayShouldBePlus61(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:46:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(61);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden45MinutenWith45minutenPause_thenGleitzeitsaldoForThisDayShouldBePlus60(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:45:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:45:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(60);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden45MinutenWith43minutenPause_thenGleitzeitsaldoForThisDayShouldBePlus60(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:43:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:45:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(60);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit9Stunden45MinutenWith60minutenPause_thenGleitzeitsaldoForThisDayShouldBePlus45(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("13:00:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("16:45:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(45);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10StundenWithoutPause_thenGleitzeitsaldoForThisDayShouldBePlus75(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);


        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(75);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10StundenWith30MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus75(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(75);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10StundenWith45MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus75(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:45:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(75);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10StundenWith46MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus74(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:46:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:00:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(74);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10Stunden45MinutenWith45MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:45:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:45:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10Stunden46MinutenWith45MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:45:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:46:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit10Stunden46MinutenWith3Mal15MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause1 = new Zeitbuchung();
        gehenPause1.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause1.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause1.setBuchungsart("gehen");
        gehenPause1.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause1);

        var kommenPause1 = new Zeitbuchung();
        kommenPause1.setUhrzeit(Time.valueOf("12:15:00"));
        kommenPause1.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause1.setBuchungsart("kommen");
        kommenPause1.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause1);

        var gehenPause2 = new Zeitbuchung();
        gehenPause2.setUhrzeit(Time.valueOf("12:30:00"));
        gehenPause2.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause2.setBuchungsart("gehen");
        gehenPause2.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause2);

        var kommenPause2 = new Zeitbuchung();
        kommenPause2.setUhrzeit(Time.valueOf("12:45:00"));
        kommenPause2.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause2.setBuchungsart("kommen");
        kommenPause2.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause2);

        var gehenPause3 = new Zeitbuchung();
        gehenPause3.setUhrzeit(Time.valueOf("13:00:00"));
        gehenPause3.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause3.setBuchungsart("gehen");
        gehenPause3.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause3);

        var kommenPause3 = new Zeitbuchung();
        kommenPause3.setUhrzeit(Time.valueOf("13:15:00"));
        kommenPause3.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause3.setBuchungsart("kommen");
        kommenPause3.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause3);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("17:46:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit11Stunden1MinutenWith60MinutePause_thenGleitzeitsaldoForThisDayShouldBePlus120(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("13:00:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("18:01:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(120);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith30MinutenPauseAberGehenbuchungZurPauseVergessenUndSpäterKorrigiert_thenGleitzeitsaldoForThisDayShouldBe0(){
        //arrange
        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(0);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith30MinutenPauseAberKommenbuchungZuArbeitsbeginnVergessenUndSpäterKorrigiert_thenGleitzeitsaldoForThisDayShouldBe0(){
        //arrange

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);


        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(0);
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void arbeitszeit8Stunden30MinutenWith30MinutenPauseAberReihenfolgeKomplettVerdreht_thenGleitzeitsaldoForThisDayShouldBe0(){
        //arrange
        var gehenEnde = new Zeitbuchung();
        gehenEnde.setUhrzeit(Time.valueOf("15:30:00"));
        gehenEnde.setDatum(Date.valueOf(LocalDate.now()));
        gehenEnde.setBuchungsart("gehen");
        gehenEnde.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenEnde);

        var kommenPause = new Zeitbuchung();
        kommenPause.setUhrzeit(Time.valueOf("12:30:00"));
        kommenPause.setDatum(Date.valueOf(LocalDate.now()));
        kommenPause.setBuchungsart("kommen");
        kommenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenPause);

        var gehenPause = new Zeitbuchung();
        gehenPause.setUhrzeit(Time.valueOf("12:00:00"));
        gehenPause.setDatum(Date.valueOf(LocalDate.now()));
        gehenPause.setBuchungsart("gehen");
        gehenPause.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(gehenPause);

        var kommenBegin = new Zeitbuchung();
        kommenBegin.setUhrzeit(Time.valueOf("07:00:00"));
        kommenBegin.setDatum(Date.valueOf(LocalDate.now()));
        kommenBegin.setBuchungsart("kommen");
        kommenBegin.setPersonalnummer(100);
        zeitbuchungService.addZeitbuchung(kommenBegin);


        //actual
        var actual = service.getLatestGleitzeitByPersonalnummer(100);

        //assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getGleitzeitsaldo()).isEqualTo(0);
    }



    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(2)
    void resetZeitbuchungAndGleitzeit(){
    }
}
