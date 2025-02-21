package com.safetynet.SafetyNetAlerts.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;

@ToString
@Getter
@Setter
public class FloodStationResponse {
    private String address;
    private int stationNumber;
    private List<ResidentInfo> persons;

    public FloodStationResponse(String key, List<ResidentInfo> value) {
        this.address = key;
        this.persons = value;
    }
}