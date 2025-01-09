package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Model.FireStationModel;

import com.safetynet.SafetyNetAlerts.Service.FireStationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Data
@RestController
@RequestMapping("/fireStation")
public class FireStationController {
    @Autowired
    private FireStationService fireStationService;

    @GetMapping
    public List<FireStationModel> getAllFireStation() {
        return fireStationService.findAll();
    }

    @PostMapping
    public void addFireStation(@RequestBody FireStationModel fireStation) {
        fireStationService.save(fireStation);
    }
    @PutMapping
    public FireStationModel updateFireStation(
            @RequestBody FireStationModel updatedFireStation) {
        return fireStationService.update(updatedFireStation);
    }
    @DeleteMapping
    public void deleteFireStation(@RequestBody FireStationModel fireStation) {
        fireStationService.delete(fireStation);
    }
}


