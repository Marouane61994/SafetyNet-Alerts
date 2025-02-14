package com.safetynet.SafetyNetAlerts.Response;


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
    private List<FloodStationResponse.ResidentInfo> residents;

    public FireResponse(String address, int stationNumber, List<FloodStationResponse.ResidentInfo> residents) {
        this.address = address;
        this.stationNumber = stationNumber;
        this.residents = residents;
    }
}