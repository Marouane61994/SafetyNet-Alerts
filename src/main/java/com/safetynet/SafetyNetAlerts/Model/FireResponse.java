package com.safetynet.SafetyNetAlerts.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireResponse {

    private String address;
    private int stationNumber;
    private List<FloodStationResponse.ResidentInfo> residents;

}
