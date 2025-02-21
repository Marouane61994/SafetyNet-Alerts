package com.safetynet.SafetyNetAlerts.response;

import lombok.Data;

@Data
public class ChildInfo {
    private String firstName;
    private String lastName;
    private int age;


    public ChildInfo(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
