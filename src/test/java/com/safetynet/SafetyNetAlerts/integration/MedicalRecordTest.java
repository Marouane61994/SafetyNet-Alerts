package com.safetynet.SafetyNetAlerts.integration;


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
public class MedicalRecordTest {

    @Autowired
    public MockMvc mockMvc;


    @Test
    public void testGetAllMedicalRecords() throws Exception {
        mockMvc.perform(get("/medicalRecord")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddMedicalRecord() throws Exception {
        String newMedicalRecord = """
                {
                    "firstName": "Felicia",
                    "lastName": "Boyd",
                   "birthdate": "01/08/1986",
                    "medications": ["tetracyclaz:650mg"],
                    "allergies": ["xilliathal"]
                }
                """;

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMedicalRecord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Felicia"))
                .andExpect(jsonPath("$.lastName").value("Boyd"))
                .andExpect(jsonPath("$.birthdate").value("01/08/1986"))
                .andExpect(jsonPath("$.medications").value("tetracyclaz:650mg"))
                .andExpect(jsonPath("$.allergies").value("xilliathal"));
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        String updatedMedicalRecord = """
                {
                    "birthdate": "01/08/1986",
                    "medications": ["tetracyclaz:650mg"],
                    "allergies": ["xilliathal"]
                }
                """;

        mockMvc.perform(put("/medicalRecord/Felicia/Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMedicalRecord))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate").value("01/08/1986"))
                .andExpect(jsonPath("$.medications[0]").value("tetracyclaz:650mg"))
                .andExpect(jsonPath("$.allergies[0]").value("xilliathal"));
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/medicalRecord/John/Boyd"))
                .andExpect(status().isOk());
    }

    @BeforeEach
    public void setupData() throws IOException {
        Files.copy(Path.of("src/main/resources/databackup.json"), new FileOutputStream("src/main/resources/data.json"));
    }
}
