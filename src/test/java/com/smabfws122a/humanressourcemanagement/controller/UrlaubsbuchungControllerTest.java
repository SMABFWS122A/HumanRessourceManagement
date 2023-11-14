package com.smabfws122a.humanressourcemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.smabfws122a.humanressourcemanagement.entity.Mitarbeiter;
import com.smabfws122a.humanressourcemanagement.entity.Urlaubsbuchung;
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
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UrlaubsbuchungControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Urlaubsbuchung validUrlaubsbuchung10= new Urlaubsbuchung();
    private final Urlaubsbuchung validUrlaubsbuchung20 = new Urlaubsbuchung();
    private final Urlaubsbuchung updatedUrlaubsbuchung10 = new Urlaubsbuchung();
    private final Mitarbeiter mitarbeiter10 = new Mitarbeiter();



    @BeforeAll
    void setUp(){
        mitarbeiter10.setPersonalnummer(-4);
        mitarbeiter10.setVorname("Max");
        mitarbeiter10.setNachname("Meier");
        mitarbeiter10.setEmail("maxmeier@email.com");
        mitarbeiter10.setBeschaeftigungsgrad_id(1);

        validUrlaubsbuchung10.setVonDatum(Date.valueOf(LocalDate.now().minusDays(2)));
        validUrlaubsbuchung10.setBisDatum(Date.valueOf(LocalDate.now().plusDays(4)));
        validUrlaubsbuchung10.setBuchungsart("Tarifurlaub");
        validUrlaubsbuchung10.setPersonalnummer(100);


        validUrlaubsbuchung20.setVonDatum(Date.valueOf("2023-10-23"));
        validUrlaubsbuchung20.setBisDatum(Date.valueOf("2023-10-23"));
        validUrlaubsbuchung20.setBuchungsart("Tarifurlaub");
        validUrlaubsbuchung20.setPersonalnummer(100);

        updatedUrlaubsbuchung10.setId(7);
        updatedUrlaubsbuchung10.setVonDatum(Date.valueOf(LocalDate.now()));
        updatedUrlaubsbuchung10.setBisDatum(Date.valueOf(LocalDate.now().plusDays(4)));
        updatedUrlaubsbuchung10.setBuchungsart("Tarifurlaub");
        updatedUrlaubsbuchung10.setPersonalnummer(100);

    }

    @Test
    @Order(1)
    void getAllUrlaubsbuchungen_checkNumberOfEntitiesBeforeAddingTestData_thenStatusOkAndMustBe6() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/urlaubsbuchungen")
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
    void postUrlaubsbuchung_whenModelIsValid_thenStatusOk() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(validUrlaubsbuchung10);
        this.mockMvc.perform(
                        post("/urlaubsbuchung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        body = objectWriter.writeValueAsString(validUrlaubsbuchung20);
        this.mockMvc.perform(
                        post("/urlaubsbuchung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getAllUrlaubsbuchungen_checkNumberOfEntitiesAfterAddingTestData_thenStatusOkAndSize8() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/urlaubsbuchungen")
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
    void getUrlaubsbuchungById_whenEntityWithIdFound_ThenOkAndReturnEntity() throws Exception {
        this.mockMvc.perform(
                        get("/urlaubsbuchung/" + "7")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vonDatum").value(Date.valueOf(LocalDate.now().minusDays(2)).toString()))
                .andExpect(jsonPath("$.bisDatum").value(Date.valueOf(LocalDate.now().plusDays(4)).toString()))
                .andExpect(jsonPath("$.buchungsart").value("Tarifurlaub"))
                .andExpect(jsonPath("$.personalnummer").value(100));
    }

    @Test
    @Order(5)
    void getUrlaubsbuchungById_entityWithIdNotFound_thenNotFound() throws Exception {
        this.mockMvc.perform(
                        get("/urlaubsbuchung/" + "-5")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void putUrlaubsbuchung_whenModelIsValid_thenBadRequest() throws Exception {
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String body = objectWriter.writeValueAsString(updatedUrlaubsbuchung10);
        this.mockMvc.perform(
                        post("/urlaubsbuchung")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/urlaubsbuchung/" + "7")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vonDatum").value(Date.valueOf(LocalDate.now()).toString()))
                .andExpect(jsonPath("$.bisDatum").value(Date.valueOf(LocalDate.now().plusDays(4)).toString()))
                .andExpect(jsonPath("$.buchungsart").value("Tarifurlaub"))
                .andExpect(jsonPath("$.personalnummer").value(100));
    }




@Test
@Order(7)
void getUrlaubstageByPersonalnummerAndMonatAndJahr_entityWithIdNotFound_thenNotFound() throws Exception {
    MvcResult mvcResult  = this.mockMvc.perform(
                    get("/urlaubsbuchungen/" +"100" + "/" + "2023-10-01")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

            String contentAsString = mvcResult.getResponse().getContentAsString();
            List<String> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
            });
            assertThat(result).hasSize(1);
}



    @Test
    @Order(10)
    @Sql(statements = {
            "DELETE FROM urlaubsbuchung WHERE id = 7",
            "DELETE FROM urlaubsbuchung WHERE id = 8",
            "ALTER SEQUENCE urlaubsbuchung_id_seq RESTART WITH 7;"
    })
    void getAllUrlaubsbuchungen_checkNumberOfEntitiesAfterDeletingTestData_thenStatusOkAndSize6() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/urlaubsbuchungen")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Urlaubsbuchung> result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(result).hasSize(6);
    }

}
