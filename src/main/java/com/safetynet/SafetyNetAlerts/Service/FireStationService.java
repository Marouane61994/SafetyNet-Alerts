package com.safetynet.SafetyNetAlerts.Service;


import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Data
@Service
public class FireStationService {
    @Autowired
    private FireStationRepository fireStationRepository;

    public List<FireStationModel> findAll() {
        return fireStationRepository.findAll();
    }

    public void save(FireStationModel fireStation) {
        fireStationRepository.save(fireStation);
    }

    public void delete(FireStationModel fireStation) {
        fireStationRepository.delete(fireStation);
    }
    public FireStationModel update(FireStationModel updatedFireStation) {
        // Vérifie si la caserne existe
        FireStationModel existingFireStation = (FireStationModel) fireStationRepository.findAll();


        // Mettre à jour
        existingFireStation.setAddress(updatedFireStation.getAddress());
        existingFireStation.setStation(updatedFireStation.getStation());

        // Sauvegarde lde la mise a jour
        return fireStationRepository.save(existingFireStation);
    }

}




