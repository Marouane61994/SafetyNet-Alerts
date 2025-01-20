package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.*;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@RequiredArgsConstructor
@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;

    public List<FireStationModel> getAllFireStations() {
        return fireStationRepository.findAll();
    }

    public FireStationModel addFireStation(FireStationModel fireStation) {
        return fireStationRepository.save(fireStation);
    }

    public FireStationModel updateFireStation(String address, FireStationModel updatedFireStation) {
        FireStationModel existingFireStation = fireStationRepository.findByAddress(address)
                .orElseThrow(() -> new RuntimeException("FireStation not found with address: " + address));

        existingFireStation.setStation(updatedFireStation.getStation());

        return fireStationRepository.save(existingFireStation);
    }

    public void deleteFireStation(String address) {
        fireStationRepository.deleteByAddress(address);
    }

    public List<FloodStationResponse> getFloodStations(List<Integer> stationNumbers) {
        return fireStationRepository.getFloodStations(stationNumbers);

    }


}










