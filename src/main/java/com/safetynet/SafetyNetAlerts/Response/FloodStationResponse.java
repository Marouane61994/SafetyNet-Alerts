package com.safetynet.SafetyNetAlerts.Response;

import lombok.Data;


import java.util.List;

@Data
public class FloodStationResponse {
    private String address;
    private int stationNumber;
    private List<ResidentInfo> persons;

    public FloodStationResponse(String key, List<ResidentInfo> value) {
        this.address = key;
        this.persons = value;
    }


    @Data
    public static class ResidentInfo {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private List<String> medications;
        private List<String> allergies;


        public ResidentInfo(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.phone = phone;
            this.medications = medications;
            this.allergies = allergies;

        }


        public ResidentInfo() {

        }
    }

}