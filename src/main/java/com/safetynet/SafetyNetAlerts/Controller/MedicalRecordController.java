package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;


    @GetMapping
    public List<MedicalRecordModel> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping
    public MedicalRecordModel addMedicalRecord(@RequestBody MedicalRecordModel medicalRecord) {
        return medicalRecordService.addMedicalRecord(medicalRecord);
    }

    @PutMapping("/{firstName}/{lastName}")
    public MedicalRecordModel updateMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody MedicalRecordModel updateMedicalRecord) {
        return medicalRecordService.updateMedicalRecord(firstName, lastName, updateMedicalRecord);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public String deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        medicalRecordService.deleteMedicalRecord(firstName, lastName);
        return "MedicalRecord deleted successfully";
    }
}
