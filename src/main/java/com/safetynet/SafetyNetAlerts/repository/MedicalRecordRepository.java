package com.safetynet.SafetyNetAlerts.repository;

import com.safetynet.SafetyNetAlerts.model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MedicalRecordRepository {

    private final DataLoaderService dataLoaderService;


    public List<MedicalRecordModel> findAll() {
        return dataLoaderService.getDataModel().getMedicalRecord();
    }

    public Optional<MedicalRecordModel> findByFullName(String firstName, String lastName) {
        return dataLoaderService.getDataModel().getMedicalRecord()
                .stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public MedicalRecordModel save(MedicalRecordModel medicalRecord) {
        List<MedicalRecordModel> medicalRecords = dataLoaderService.getDataModel().getMedicalRecord();

        medicalRecords.removeIf(mr -> mr.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
                && mr.getLastName().equalsIgnoreCase(medicalRecord.getLastName()));
        medicalRecords.add(medicalRecord);
        dataLoaderService.writeJsonToFile();
        dataLoaderService.getDataModel().setMedicalRecord(medicalRecords);
        return medicalRecord;
    }

    public void deleteByFullName(String firstName, String lastName) {
        List<MedicalRecordModel> medicalRecords = dataLoaderService.getDataModel().getMedicalRecord();
        medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                && medicalRecord.getLastName().equalsIgnoreCase(lastName));
        dataLoaderService.getDataModel().setMedicalRecord(medicalRecords);
        dataLoaderService.writeJsonToFile();
    }
    
}
