package com.smabfws122a.humanressourcemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.smabfws122a.humanressourcemanagement.entity.Fehlermeldung;
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
public class FehlermeldungControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Fehlermeldung validFehlermeldung10= new Fehlermeldung();
    private final Fehlermeldung validFehlermeldung20 = new Fehlermeldung();
    private final Fehlermeldung updatedFehlermeldung20 = new Fehlermeldung();



    @BeforeAll
    void setUp(){
        validFehlermeldung10.setFehlermeldung("FEHLER 10");
        validFehlermeldung10.setPersonalnummer(200);


        validFehlermeldung20.setFehlermeldung("FEHLER 20");
        validFehlermeldung20.setPersonalnummer(100);

        updatedFehlermeldung20.setFehlermeldung("FEHLER UPDATED");
        updatedFehlermeldung20.setPersonalnummer(100);

    }

    @Test
    @Order(1)
    void getFehlermeldung_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe0() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/fehlermeldung")
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
    void postFehlermeldung_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validFehlermeldung10);
        this.mockMvc.perform(
                        post("/fehlermeldung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validFehlermeldung20);
        this.mockMvc.perform(
                        post("/fehlermeldung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllFehlermeldung_checkNumberOfEntitiesAfterAddingTestData_thenStatusOkAndSize2() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/fehlermeldung")
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
    void getFehlermeldungByPersonalnummer_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/fehlermeldung/" + 100)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(100))
                .andExpect(jsonPath("$.fehlermeldung").value("FEHLER 20"));
    }

    @Test
    @Order(5)
    void getFehlermeldungByEmail_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/fehlermeldung/" + 300)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putFehlermeldung_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updatedFehlermeldung20);
        this.mockMvc.perform(
                        post("/fehlermeldung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/fehlermeldung/" + 100)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalnummer").value(100))
                .andExpect(jsonPath("$.fehlermeldung").value("FEHLER UPDATED"));
    }

    @Test
    @Order(8)
    @Sql(statements = {
            "DELETE FROM fehlermeldung WHERE personalnummer = 100",
            "DELETE FROM fehlermeldung WHERE personalnummer = 200"
    })
    void getAllFehlermeldung_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize0() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/fehlermeldung")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(0);
    }

}
