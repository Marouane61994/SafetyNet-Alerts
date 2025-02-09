package com.safetynet.SafetyNetAlerts.Response;


import lombok.Data;

import java.util.List;

@Data
public class FireStationResponse {

    private List<FireStationInfo> persons;
    private int adultCount;
    private int childCount;



    @Data
    public static class FireStationInfo {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;

    }
}