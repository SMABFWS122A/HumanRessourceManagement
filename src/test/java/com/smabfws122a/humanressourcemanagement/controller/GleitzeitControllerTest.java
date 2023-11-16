package com.smabfws122a.humanressourcemanagement.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GleitzeitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(1)
    void getLatestGleitzeitByPersonalnummer_whenEntityFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/gleitzeit/" + "100")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gleitzeitsaldo").value(0))
                .andExpect(jsonPath("$.datum").value(Date.valueOf("2023-01-01").toString()))
                .andExpect(jsonPath("$.personalnummer").value(100));
    }

    @Test
    @Sql(value = {"/db/resetZeitbuchungAndGleitzeit.sql"})
    @Order(2)
    void getLatestGleitzeitByPersonalnummer_whenEntityNotFound_ThenNotFound() throws Exception {
        this.mockMvc.perform(
                        get("/gleitzeit/" + "999")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
