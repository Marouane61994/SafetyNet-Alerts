package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Model.FireResponse;
import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Model.FloodStationResponse;
import com.safetynet.SafetyNetAlerts.Service.FireStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class FireStationController {

    private final FireStationService fireStationService;


    @GetMapping("/firestation")
    public List<FireStationModel> getAllFireStations() {
        return fireStationService.getAllFireStations();

    }

    @PostMapping("/firestation")
    public FireStationModel addFireStation(@RequestBody FireStationModel fireStation) {
        return fireStationService.addFireStation(fireStation);
    }

    @PutMapping("/firestation/{address}")
    public FireStationModel updateFireStation(@PathVariable String address, @RequestBody FireStationModel updatedFireStation) {
        return fireStationService.updateFireStation(address, updatedFireStation);
    }

    @DeleteMapping("/firestation/{address}")
    public String deleteFireStation(@PathVariable String address) {
        fireStationService.deleteFireStation(address);
        return "FireStation deleted successfully";
    }
    @GetMapping("flood/stations")
    public List<FloodStationResponse> getFloodStations(@RequestParam List<Integer> stations) {
        return fireStationService.getFloodStations(stations);
    }

    @GetMapping("/fire")
    public FireResponse getResidentsByAddress(@RequestParam String address) {
        return fireStationService.getResidentsByAddress(address);
    }

}


