package com.safetynet.SafetyNetAlerts.Model;

import lombok.Data;

import java.util.List;

@Data
public class FireStationResponse {

    private List<PersonInfo> persons;
    private int adultCount;
    private int childCount;

    @Data
    public static class PersonInfo {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;

    }
}

