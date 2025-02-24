package com.safetynet.safetynetalerts.model;

import lombok.Data;

@Data
public class PersonModel {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public String toString() {
        return "Persons{" +
                "firstName='" + firstName + '\'' +
                "lastName='" + lastName + '\'' +
                "address='" + address + '\'' +
                "city='" + city + '\'' +
                "zip='" + zip + '\'' +
                "phone='" + phone + '\'' +
                "email='" + email + '\'' +
                '}';
    }

}
