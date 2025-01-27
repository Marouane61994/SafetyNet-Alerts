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
public class FireStationTest {

    @Autowired
    public MockMvc mockMvc;


    @Test
    public void testGetAllFireStations() throws Exception {
        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddFireStation() throws Exception {
        String newFireStation = """
                {
                    "address": "123 Main St",
                    "station": "1"
                }
                """;

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFireStation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.station").value("1"));
    }

    @Test
    public void testUpdateFireStation() throws Exception {

        String updatedFireStation = """
                {
                    "station": "3"
                }
                """;

        mockMvc.perform(put("/firestation/1509 Culver St")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFireStation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("3"));
    }

    @Test
    public void testDeleteFireStation() throws Exception {

        mockMvc.perform(get("/firestation/789 Oak St"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFloodStations() throws Exception {
        mockMvc.perform(get("/flood/stations?stations=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void getResidentsByAddress() throws Exception {
        mockMvc.perform(get("/fire?address=947 E. Rose Dr"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void getPhoneNumbersByFireStation() throws Exception {
        mockMvc.perform(get("/phoneAlert?firestation=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
    @Test
    public void getChildAlert() throws Exception {
        mockMvc.perform(get("/childAlert?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
    @Test
    public void getPersonsCoveredByStation() throws Exception {
        mockMvc.perform(get("/coverage?stationNumber=4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @BeforeEach
    public void setupData() throws IOException {
        Files.copy(Path.of("src/main/resources/databackup.json"), new FileOutputStream("src/main/resources/data.json"));
    }
}
