package com.safetynet.SafetyNetAlerts.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.SafetyNetAlerts.Model.DataModel;
import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.*;


import java.util.List;
import java.util.Map;


@Component
@Data
@Service
public class DataLoaderService {
    String filePath = "src/main/resources/data.json";
    private DataModel dataModel;
    private ObjectMapper objectMapper = new ObjectMapper();

    public DataLoaderService() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        readJsonFromFile();
        writeJsonToFile();
    }


    // Méthode pour lire un fichier JSON
    public void readJsonFromFile() {
        objectMapper.registerModule(new JavaTimeModule());
        try (InputStream inputStream = getClass().getResourceAsStream("/data.json")) {
            dataModel = objectMapper.readValue(inputStream, DataModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement des données JSON", e);
        }
    }

    // Méthode pour écrire dans un fichier JSON
    public void writeJsonToFile() {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            objectMapper.writeValue(outputStream, dataModel);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture des données JSON dans le fichier", e);
        }
    }

    public List<PersonModel> getPersons() {
        return dataModel.getPersons();
    }

    public List<FireStationModel> getFireStation() {
        return dataModel.getFireStation();
    }

    public List<MedicalRecordModel> getMedicalRecord() {
        return dataModel.getMedicalRecord();
    }
}





