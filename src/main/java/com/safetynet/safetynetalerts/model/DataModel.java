package com.safetynet.safetynetalerts.model;

import lombok.Data;

import java.util.List;

@Data
public class DataModel {

    private List<PersonModel> persons;
    private List<FireStationModel> fireStation;
    private List<MedicalRecordModel> medicalRecord;

}
