package com.smabfws122a.humanressourcemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.entity.Zeitbuchung;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MitarbeiterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Mitarbeiter validMitarbeiter10= new Mitarbeiter();
    private final Mitarbeiter validMitarbeiter20 = new Mitarbeiter();
    private final Mitarbeiter updatedMitarbeiter = new Mitarbeiter();



    @BeforeAll
    void setUp(){
        validMitarbeiter10.setPersonalnummer(10);
        validMitarbeiter10.setVorname("Max");
        validMitarbeiter10.setNachname("Meier");
        validMitarbeiter10.setEmail("maxmeier@email.com");
        validMitarbeiter10.setBeschaeftigungsgrad_id(1);

        validMitarbeiter20.setPersonalnummer(20);
        validMitarbeiter20.setVorname("Maria");
        validMitarbeiter20.setNachname("Meier");
        validMitarbeiter20.setEmail("mariameier@email.com");
        validMitarbeiter20.setBeschaeftigungsgrad_id(2);

        updatedMitarbeiter.setPersonalnummer(10);
        updatedMitarbeiter.setVorname("Max");
        updatedMitarbeiter.setNachname("Meier");
        updatedMitarbeiter.setEmail("maxmeier@email.com");
        updatedMitarbeiter.setBeschaeftigungsgrad_id(3);

    }

    @Test
    @Order(1)
    void getMitarbeiter_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(5);
    }

    @Test
    @Order(2)
    void postMitarbeiter_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validMitarbeiter10);
        this.mockMvc.perform(
                        post("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validMitarbeiter20);
        this.mockMvc.perform(
                        post("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllMitarbeiter_checkNumberOfEntitiesAfterAddingTestData_thenStatusOkAndSize7() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(7);
    }

    @Test
    @Order(4)
    void getMitarbeiterByPersonalnummer_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/mitarbeiter/" + 10)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(10))
                .andExpect(jsonPath("$.vorname").value("Max"))
                .andExpect(jsonPath("$.nachname").value("Meier"))
                .andExpect(jsonPath("$.email").value("maxmeier@email.com"))
                .andExpect(jsonPath("$.beschaeftigungsgrad_id").value(1));
    }

    @Test
    @Order(5)
    void getMitarbeiterByPersonalnummer_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/mitarbeiter/" + 999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putMitarbeiter_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updatedMitarbeiter);
        this.mockMvc.perform(
                        post("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/mitarbeiter/" + 10)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(10))
                .andExpect(jsonPath("$.vorname").value("Max"))
                .andExpect(jsonPath("$.nachname").value("Meier"))
                .andExpect(jsonPath("$.email").value("maxmeier@email.com"))
                .andExpect(jsonPath("$.beschaeftigungsgrad_id").value(3));
    }

    @Test
    @Order(7)
    void deleteMitarbeiterByPersonalnummer_checkNumberOfEntitiesAfterDeletingOneTestData_thenStatusOkAndSize6() throws Exception {
        this.mockMvc.perform(
                        delete("/mitarbeiter/" + 20)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(6);
    }

    @Test
    @Order(8)
    @Sql(statements = {
            "DELETE FROM mitarbeiter WHERE Personalnummer = 10",
            "DELETE FROM mitarbeiter WHERE Personalnummer = 20"
    })
    void getAllMitarbeiter_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/mitarbeiter")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(5);
    }

}
