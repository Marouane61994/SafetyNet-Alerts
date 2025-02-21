package com.safetynet.SafetyNetAlerts.controller;

import com.safetynet.SafetyNetAlerts.model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    private final MedicalRecordService medicalRecordService;

    @GetMapping
    public List<MedicalRecordModel> getAllMedicalRecords() {
        logger.info("GET /medicalRecord - Fetching all medical records");
        List<MedicalRecordModel> records = medicalRecordService.getAllMedicalRecords();
        logger.info("Response: {}", records);
        return records;
    }

    @PostMapping
    public MedicalRecordModel addMedicalRecord(@RequestBody MedicalRecordModel medicalRecord) {
        logger.info("POST /medicalRecord - Adding new medical record: {}", medicalRecord);
        MedicalRecordModel createdRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        logger.info("Medical record added: {}", createdRecord);
        return createdRecord;
    }

    @PutMapping("/{firstName}/{lastName}")
    public MedicalRecordModel updateMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody MedicalRecordModel updateMedicalRecord) {
        logger.info("PUT /medicalRecord/{}/{} - Updating medical record: {}", firstName, lastName, updateMedicalRecord);
        MedicalRecordModel updatedRecord = medicalRecordService.updateMedicalRecord(firstName, lastName, updateMedicalRecord);
        logger.info("Updated medical record: {}", updatedRecord);
        return updatedRecord;
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public String deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("DELETE /medicalRecord/{}/{} - Deleting medical record", firstName, lastName);
        medicalRecordService.deleteMedicalRecord(firstName, lastName);
        logger.info("Medical record deleted successfully for: {} {}", firstName, lastName);
        return "MedicalRecord deleted successfully";
    }
}
