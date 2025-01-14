package com.safetynet.SafetyNetAlerts.Repository;

import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return fireStation;
    }

    public void deleteByAddress(String address) {
        List<FireStationModel> fireStations = dataLoaderService.getDataModel().getFireStation();
        fireStations.removeIf(fireStation -> fireStation.getAddress().equalsIgnoreCase(address));
        dataLoaderService.getDataModel().setFireStation(fireStations);
    }
}
