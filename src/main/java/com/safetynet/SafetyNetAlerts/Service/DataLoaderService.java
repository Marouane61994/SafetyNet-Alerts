package com.safetynet.SafetyNetAlerts.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.SafetyNetAlerts.Model.DataModel;


import lombok.Getter;

import org.springframework.stereotype.Service;

import java.io.*;


@Getter
@Service
public class DataLoaderService {
    private DataModel dataModel;

    public DataLoaderService() throws IOException {
        readJsonFromFile();

    }

    public void readJsonFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inputStream = getClass().getResourceAsStream("/data.json");
            dataModel = objectMapper.readValue(inputStream, DataModel.class);



    }

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
