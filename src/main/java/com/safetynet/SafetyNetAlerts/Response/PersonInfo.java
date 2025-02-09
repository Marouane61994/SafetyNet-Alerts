package com.safetynet.SafetyNetAlerts.Response;



import lombok.AllArgsConstructor;
import lombok.Data;



import java.util.List;

@AllArgsConstructor
@Data
public class PersonInfo {
    private String firstName;
    private String lastName;
    private String address;
    private int age;
    private String email;
    private List<String> medications;
    private List<String> allergies;


    public String toString() {
        return "Persons{" +
                "firstName='" + firstName + '\'' +
                "lastName='" + lastName + '\'' +
                "address='" + address + '\'' +
                "medications='" + medications + '\'' +
                "allergies='" + allergies + '\'' +
                "email='" + email + '\'' +
                '}';
    }
}