package com.safetynet.SafetyNetAlerts.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class FireResponse {

    private String address;
    private int stationNumber;
    private List<ResidentInfo> residents;

    public FireResponse(String address, int stationNumber, List<ResidentInfo> residents) {
        this.address = address;
        this.stationNumber = stationNumber;
        this.residents = residents;
    }
}