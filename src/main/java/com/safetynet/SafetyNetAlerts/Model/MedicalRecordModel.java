package com.safetynet.SafetyNetAlerts.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class MedicalRecordModel {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate birthdate;
    private String firstName;
    private String lastName;
    private List<String> medications;
    private List<String> allergies;


    @Override
    public String toString() {
        return "medicalRecords{" +
                "firstName='" + firstName + '\'' +
                "lastName='" + lastName + '\'' +
                "birthdate='" + birthdate + '\'' +
                "medications='" + medications + '\'' +
                "allergies='" + allergies + '\'' +
                '}';
    }
}
