package com.safetynet.SafetyNetAlerts.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.SafetyNetAlerts.model.DataModel;


import lombok.Getter;

import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Service responsible for loading and saving data from/to a JSON file.
 * It initializes the application's data by reading the file and allows updates to be written back.
 */
@Getter
@Service
public class DataLoaderService {
    private DataModel dataModel;

    /**
     * Constructor that initializes the data by reading from the JSON file.
     *
     * @throws IOException if an error occurs while reading the file.
     */
    public DataLoaderService() throws IOException {
        readJsonFromFile();

    }

    /**
     * Reads data from the JSON file and loads it into memory.
     *
     * @throws IOException if the file cannot be read or is not found.
     */
    public void readJsonFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inputStream = getClass().getResourceAsStream("/data.json");
        dataModel = objectMapper.readValue(inputStream, DataModel.class);


    }

    /**
     * Writes the current state of the data to the JSON file.
     * If an error occurs during writing, a runtime exception is thrown.
     */
    public void writeJsonToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        String filePath = "src/main/resources/data.json";
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            objectMapper.writeValue(outputStream, dataModel);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON data to file", e);
        }
    }

}
