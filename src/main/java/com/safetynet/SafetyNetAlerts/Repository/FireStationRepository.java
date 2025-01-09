package com.safetynet.SafetyNetAlerts.Repository;


import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Data
@Repository
public class FireStationRepository {
    @Autowired
    private DataLoaderService dataLoaderService;

    public List<FireStationModel> findAll() {
        return dataLoaderService.getFireStation();
    }

    public FireStationModel save(FireStationModel fireStation) {
        dataLoaderService.getFireStation().add(fireStation);
        return fireStation;
    }

    public void delete(FireStationModel fireStation) {
        dataLoaderService.getFireStation().remove(fireStation);
    }
}

