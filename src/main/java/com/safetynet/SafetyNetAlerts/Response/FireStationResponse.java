package com.safetynet.SafetyNetAlerts.Response;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class FireStationResponse {

    private List<FireStationInfo> persons;
    private int adultCount;
    private int childCount;



    @ToString
    @Getter
    @Setter
    public static class FireStationInfo {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;

    }
}