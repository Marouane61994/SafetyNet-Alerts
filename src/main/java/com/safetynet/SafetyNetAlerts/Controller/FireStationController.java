package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Service.FireStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;


    @GetMapping
    public List<FireStationModel> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    @PostMapping
    public FireStationModel addFireStation(@RequestBody FireStationModel fireStation) {
        return fireStationService.addFireStation(fireStation);
    }

    @PutMapping("/{address}")
    public FireStationModel updateFireStation(@PathVariable String address, @RequestBody FireStationModel updatedFireStation) {
        return fireStationService.updateFireStation(address, updatedFireStation);
    }

    @DeleteMapping("/{address}")
    public String deleteFireStation(@PathVariable String address) {
        fireStationService.deleteFireStation(address);
        return "FireStation deleted successfully";
    }
}

