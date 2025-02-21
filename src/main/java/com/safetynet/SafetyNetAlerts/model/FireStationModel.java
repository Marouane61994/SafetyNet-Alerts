package com.safetynet.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class FireStationModel {
    private String address;
    private String station;


    @Override
    public String toString() {
        return "fireStations{" +
                "address='" + address + '\'' +
                "station='" + station + '\'' +
                '}';
    }
}

