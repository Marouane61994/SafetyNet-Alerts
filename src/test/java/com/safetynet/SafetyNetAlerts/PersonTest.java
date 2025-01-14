package com.safetynet.SafetyNetAlerts;



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

    @BeforeEach
    public void setupData() throws IOException {
        Files.copy(Path.of("src/main/resources/databackup.json"), new FileOutputStream("src/main/resources/data.json"));
    }
}
