package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.*;
import com.safetynet.safetynetalerts.service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class FireStationRepository {

    private final DataLoaderService dataLoaderService;


    public List<FireStationModel> findAll() {
        return dataLoaderService.getDataModel().getFireStation();
    }

    public Optional<FireStationModel> findByAddress(String address) {
        return dataLoaderService.getDataModel().getFireStation()
                .stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    public FireStationModel save(FireStationModel fireStation) {
        List<FireStationModel> fireStations = dataLoaderService.getDataModel().getFireStation();

        fireStations.removeIf(fs -> fs.getAddress().equalsIgnoreCase(fireStation.getAddress()));
        fireStations.add(fireStation);

        dataLoaderService.getDataModel().setFireStation(fireStations);
        dataLoaderService.writeJsonToFile();
        return fireStation;
    }

    public void deleteByAddress(String address) {
        List<FireStationModel> fireStations = dataLoaderService.getDataModel().getFireStation();
        fireStations.removeIf(fireStation -> fireStation.getAddress().equalsIgnoreCase(address));
        dataLoaderService.getDataModel().setFireStation(fireStations);
        dataLoaderService.writeJsonToFile();
    }

    public List<String> findAddressesByStationNumber(String stationNumber) {
        return dataLoaderService.getDataModel().getFireStation().stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber)) // Filtre les casernes
                .map(FireStationModel::getAddress) // Extrait les adresses
                .collect(Collectors.toList());
    }


}



