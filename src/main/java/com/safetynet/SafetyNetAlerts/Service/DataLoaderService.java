package com.safetynet.SafetyNetAlerts.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.safetynet.SafetyNetAlerts.Model.DataModel;
import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@Getter
@Data
@Service
public class DataLoaderService {

    private DataModel dataModel;


    public DataLoaderService() {

        ObjectMapper objectMapper = new ObjectMapper();

        // Enregistrer le module JavaTimeModule pour gérer LocalDate
        JavaTimeModule module = new JavaTimeModule();

        objectMapper.registerModule(new JavaTimeModule());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        objectMapper.registerModule(module);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (InputStream inputStream = getClass().getResourceAsStream("/data.json")) {
            dataModel = objectMapper.readValue(inputStream, DataModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement des données JSON", e);
        }


    }


    public List<PersonModel> getPersons() {
        return dataModel.getPersons();
    }

    public List<FireStationModel> getFirestations() {
        return dataModel.getFirestations();
    }

    public List<MedicalRecordModel> getMedicalrecords() {
        return dataModel.getMedicalrecords();
    }
}



