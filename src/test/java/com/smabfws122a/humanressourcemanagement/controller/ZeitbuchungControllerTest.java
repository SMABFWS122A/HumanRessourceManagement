package com.smabfws122a.humanressourcemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

import java.sql.Date;
import java.sql.Time;
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
public class ZeitbuchungControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Zeitbuchung validKommenBuchung = new Zeitbuchung();
    private final Zeitbuchung validGehenBuchung = new Zeitbuchung();
    private final Zeitbuchung updateZeitbuchung = new Zeitbuchung();



    @BeforeAll
    void setUp(){
        validKommenBuchung.setUhrzeit(Time.valueOf("07:00:00"));
        validKommenBuchung.setDatum(Date.valueOf("2023-10-23"));
        validKommenBuchung.setBuchungsart("kommen");
        validKommenBuchung.setPersonalnummer(100);

        validGehenBuchung.setUhrzeit(Time.valueOf("15:00:00"));
        validGehenBuchung.setDatum(Date.valueOf("2023-10-23"));
        validGehenBuchung.setBuchungsart("gehen");
        validGehenBuchung.setPersonalnummer(100);

        updateZeitbuchung.setId(1);
        updateZeitbuchung.setUhrzeit(Time.valueOf("08:00:00"));
        updateZeitbuchung.setDatum(Date.valueOf("2023-10-23"));
        updateZeitbuchung.setBuchungsart("kommen");
        updateZeitbuchung.setPersonalnummer(100);

    }

    @Test
    @Order(1)
    void getAllZeitbuchungenByPersonalnummerAndDatum_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/zeitbuchungen/"+ 100 + "/" + Date.valueOf("2023-10-23"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(0);
    }

    @Test
    @Order(2)
    void postZeitbuchung_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validKommenBuchung);
        this.mockMvc.perform(
                post("/zeitbuchung")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validGehenBuchung);
        this.mockMvc.perform(
                        post("/zeitbuchung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllZeitbuchungenByPersonalnummerAndDatum_checkNumberOfEntitiesAfterAddingTestData_thenStatusOkAndSize2() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/zeitbuchungen/"+ 100 + "/" + Date.valueOf("2023-10-23"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(2);
    }

    @Test
    @Order(4)
    void getZeitbuchungById_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                get("/zeitbuchung/" + 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uhrzeit").value(Time.valueOf("07:00:00").toString()))
                .andExpect(jsonPath("$.datum").value(Date.valueOf("2023-10-23").toString()))
                .andExpect(jsonPath("$.buchungsart").value("kommen"))
                .andExpect(jsonPath("$.personalnummer").value(100));
    }

    @Test
    @Order(5)
    void getZeitbuchungbyId_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/zeitbuchnung/" + 999)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putZeitbuchung_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updateZeitbuchung);
        this.mockMvc.perform(
                        post("/zeitbuchung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/zeitbuchung/" + 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uhrzeit").value(Time.valueOf("08:00:00").toString()))
                .andExpect(jsonPath("$.datum").value(Date.valueOf("2023-10-23").toString()))
                .andExpect(jsonPath("$.buchungsart").value("kommen"))
                .andExpect(jsonPath("$.personalnummer").value(100));
    }

    @Test
    @Order(7)
    void deleteZeitbuchungenById_checkNumberOfEntitiesAfterDeletingOneTestData_thenStatusOkAndSize1() throws Exception {
        this.mockMvc.perform(
                        delete("/zeitbuchung/" + 2)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/zeitbuchungen/"+ 100 + "/" + Date.valueOf("2023-10-23"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(1);
    }

    @Test
    @Order(8)
    @Sql(statements = {
            "DELETE FROM zeitbuchung",
            "ALTER SEQUENCE zeitbuchung_id_seq RESTART;"
    })
    void getAllZeitbuchungenByPersonalnummerAndDatum_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize0() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/zeitbuchungen/"+ 100 + "/" + Date.valueOf("2023-10-23"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(0);
    }

}
