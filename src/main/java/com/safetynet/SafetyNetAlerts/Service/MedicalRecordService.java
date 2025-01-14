package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;


    public List<MedicalRecordModel> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecordModel addMedicalRecord(MedicalRecordModel medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecordModel updateMedicalRecord(String firstName, String lastName, MedicalRecordModel updatedMedicalRecord) {
        MedicalRecordModel existingMedicalRecord = medicalRecordRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found for: " + firstName + " " + lastName));

        existingMedicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
        existingMedicalRecord.setMedications(updatedMedicalRecord.getMedications());
        existingMedicalRecord.setAllergies(updatedMedicalRecord.getAllergies());

        return medicalRecordRepository.save(existingMedicalRecord);
    }

    public void deleteMedicalRecord(String firstName, String lastName) {
        medicalRecordRepository.deleteByFullName(firstName, lastName);
    }
}
