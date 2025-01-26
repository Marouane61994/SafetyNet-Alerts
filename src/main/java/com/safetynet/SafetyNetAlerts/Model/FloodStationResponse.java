package com.safetynet.SafetyNetAlerts.Model;

import lombok.Data;


import java.util.List;

@Data
public class FloodStationResponse {
    private String address;
    private int stationNumber;
    private List<ResidentInfo> persons;



    @Data
    public static class ResidentInfo {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private List<String> medications;
        private List<String> allergies;


    }

}
