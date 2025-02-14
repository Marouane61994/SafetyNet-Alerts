package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing medical records.
 * Provides functionalities to retrieve, add, update, and delete medical records.
 */
@RequiredArgsConstructor
@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Retrieves all medical records.
     *
     * @return a list of all {@link MedicalRecordModel} instances.
     */
    public List<MedicalRecordModel> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    /**
     * Adds a new medical record.
     *
     * @param medicalRecord The medical record to be added.
     * @return the saved {@link MedicalRecordModel}.
     */
    public MedicalRecordModel addMedicalRecord(MedicalRecordModel medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    /**
     * Updates an existing medical record.
     *
     * @param firstName         The first name of the person whose medical record needs to be updated.
     * @param lastName          The last name of the person whose medical record needs to be updated.
     * @param updatedMedicalRecord The updated medical record data.
     * @return the updated {@link MedicalRecordModel}.
     * @throws RuntimeException if no medical record is found for the given person.
     */
    public MedicalRecordModel updateMedicalRecord(String firstName, String lastName, MedicalRecordModel updatedMedicalRecord) {
        MedicalRecordModel existingMedicalRecord = medicalRecordRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found for: " + firstName + " " + lastName));

        existingMedicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
        existingMedicalRecord.setMedications(updatedMedicalRecord.getMedications());
        existingMedicalRecord.setAllergies(updatedMedicalRecord.getAllergies());

        return medicalRecordRepository.save(existingMedicalRecord);
    }

    /**
     * Deletes a medical record by first and last name.
     *
     * @param firstName The first name of the person whose medical record needs to be deleted.
     * @param lastName  The last name of the person whose medical record needs to be deleted.
     */
    public void deleteMedicalRecord(String firstName, String lastName) {
        medicalRecordRepository.deleteByFullName(firstName, lastName);
    }
}
