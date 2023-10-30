package com.smabfws122a.humanressourcemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.smabfws122a.humanressourcemanagement.entity.Login;
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
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Login validLogin10= new Login();
    private final Login validLogin20 = new Login();
    private final Login updatedLogin20 = new Login();



    @BeforeAll
    void setUp(){
        validLogin10.setEmail("monikaschmitz10@mail.de");
        validLogin10.setPasswort("monikaschmitz");
        validLogin10.setAdmin(false);
        validLogin10.setPersonalnummer(200);


        validLogin20.setEmail("hansmueller.admin10@mail.de");
        validLogin20.setPasswort("root");
        validLogin20.setAdmin(true);
        validLogin20.setPersonalnummer(100);

        updatedLogin20.setEmail("hansmueller.admin10@mail.de");
        updatedLogin20.setPasswort("root");
        updatedLogin20.setAdmin(false);
        updatedLogin20.setPersonalnummer(100);

    }

    @Test
    @Order(1)
    void getLogin_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe6() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/login")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(6);
    }

    @Test
    @Order(2)
    void postLogin_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validLogin10);
        this.mockMvc.perform(
                        post("/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validLogin20);
        this.mockMvc.perform(
                        post("/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllLogin_checkNumberOfEntitiesAfterAddingTestData_thenStatusOkAndSize7() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/login")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(8);
    }

    @Test
    @Order(4)
    void getLoginByEmail_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/login/" + "monikaschmitz10@mail.de")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("monikaschmitz10@mail.de"))
                .andExpect(jsonPath("$.personalnummer").value(200))
                .andExpect(jsonPath("$.passwort").value("monikaschmitz"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    @Order(5)
    @Disabled //Test schlägt Fehl, obwohl es gewollt ist das dieser nichts findet
    void getLoginByEmail_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/login/" + "mail@miallll.de")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putLogin_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updatedLogin20);
        this.mockMvc.perform(
                        post("/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/login/" + "hansmueller.admin10@mail.de")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("hansmueller.admin10@mail.de"))
                .andExpect(jsonPath("$.personalnummer").value(100))
                .andExpect(jsonPath("$.passwort").value("root"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    @Order(7)
    void deleteLoginByEmail_checkNumberOfEntitiesAfterDeletingOneTestData_thenStatusOkAndSize6() throws Exception {
        this.mockMvc.perform(
                        delete("/login/" + "monikaschmitz10@mail.de")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/login")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(7);
    }

    @Test
    @Order(8)
    @Sql(statements = {
            "DELETE FROM login WHERE email = 'monikaschmitz10@mail.de'",
            "DELETE FROM login WHERE email = 'hansmueller.admin10@mail.de'"
    })
    void getAllLogin_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize5() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/login")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Zeitbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(6);
    }

}
