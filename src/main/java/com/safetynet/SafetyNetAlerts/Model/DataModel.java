package com.safetynet.SafetyNetAlerts.Model;

import lombok.Data;

import java.util.List;

@Data
public class DataModel {

    private List<PersonModel> persons;
    private List<FireStationModel> fireStation;
    private List<MedicalRecordModel> medicalRecord;

}
