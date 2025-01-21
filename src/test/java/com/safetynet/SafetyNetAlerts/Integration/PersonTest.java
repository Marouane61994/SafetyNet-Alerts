package com.safetynet.SafetyNetAlerts.Integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void testAddPerson() throws Exception {
        String newPerson = """
                {
                    "firstName": "Jane",
                    "lastName": "Doe",
                    "address": "29 Elm St",
                    "city": "Springfield",
                    "zip": "54321",
                    "phone": "987-654-3210",
                    "email": "jane.doe@email.com"
                }
                """;

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPerson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testUpdatePerson() throws Exception {


        String updatedPerson = """
                {
                    "address": "29 Elm St",
                    "city": "Springfield",
                    "zip": "54321",
                    "phone": "987-654-3210",
                    "email": "john.doe@email.com"
                }
                """;

        mockMvc.perform(put("/person/Roger/Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPerson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("29 Elm St"))
                .andExpect(jsonPath("$.city").value("Springfield"));
    }

    @Test
    public void testDeletePerson() throws Exception {


        mockMvc.perform(delete("/person/Roger/Boyd"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCommunityEmails() throws Exception {
        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(15))
                .andExpect(jsonPath("$[0]").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[1]").value( "drk@email.com"))
                .andExpect(jsonPath("$[2]").value( "tenz@email.com"))
                .andExpect(jsonPath("$[3]").value("tcoop@ymail.com"))
                .andExpect(jsonPath("$[4]").value( "lily@email.com"))
                .andExpect(jsonPath("$[5]").value("soph@email.com"))
                .andExpect(jsonPath("$[6]").value( "ward@email.com"))
                .andExpect(jsonPath("$[7]").value("zarc@email.com"))
                .andExpect(jsonPath("$[8]").value("reg@email.com"))
                .andExpect(jsonPath("$[9]").value( "jpeter@email.com"))
                .andExpect(jsonPath("$[10]").value( "aly@imail.com"))
                .andExpect(jsonPath("$[11]").value(  "bstel@email.com"))
                .andExpect(jsonPath("$[12]").value( "ssanw@email.com"))
                .andExpect(jsonPath("$[13]").value( "clivfd@ymail.com"))
                .andExpect(jsonPath("$[14]").value( "gramps@email.com"));
    }

    @Test
    public void testGetPersonInfoByLastName() throws Exception {
        mockMvc.perform(get("/personInfolastName?lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jacob"))
                .andExpect(jsonPath("$[2].firstName").value("Tenley"))
                .andExpect(jsonPath("$[3].firstName").value("Roger"))
                .andExpect(jsonPath("$[4].firstName").value("Felicia"))
                .andExpect(jsonPath("$[5].firstName").value("Allison"));
    }

    @BeforeEach
    public void setupData() throws IOException {
        Files.copy(Path.of("src/main/resources/databackup.json"), new FileOutputStream("src/main/resources/data.json"));
    }
}
