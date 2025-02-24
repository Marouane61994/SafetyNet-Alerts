package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.FireStationModel;
import com.safetynet.safetynetalerts.response.ChildAlertResponse;
import com.safetynet.safetynetalerts.response.FireResponse;
import com.safetynet.safetynetalerts.response.FireStationResponse;
import com.safetynet.safetynetalerts.response.FloodStationResponse;
import com.safetynet.safetynetalerts.service.FireStationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FireStationController {

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);
    private final FireStationService fireStationService;

    @GetMapping("/firestations")
    public List<FireStationModel> getAllFireStations() {
        logger.info("GET /firestations - Fetching all fire stations");
        List<FireStationModel> stations = fireStationService.getAllFireStations();
        logger.info("Stations: {}", stations);
        return stations;
    }

    @PostMapping("/firestation")
    public FireStationModel addFireStation(@RequestBody FireStationModel fireStation) {
        logger.info("POST /firestation - Adding new fire station: {}", fireStation);
        FireStationModel createdFireStation = fireStationService.addFireStation(fireStation);
        logger.info("Fire station added: {}", createdFireStation);
        return createdFireStation;
    }

    @PutMapping("/firestation/{address}")
    public FireStationModel updateFireStation(@PathVariable String address, @RequestBody FireStationModel updatedFireStation) {
        logger.info("PUT /firestation/{} - Updating fire station: {}", address, updatedFireStation);
        FireStationModel updated = fireStationService.updateFireStation(address, updatedFireStation);
        logger.info("Updated fire station: {}", updated);
        return updated;
    }

    @DeleteMapping("/firestation/{address}")
    public String deleteFireStation(@PathVariable String address) {
        logger.info("DELETE /firestation/{} - Deleting fire station", address);
        fireStationService.deleteFireStation(address);
        logger.info("Fire station deleted successfully: {}", address);
        return "FireStation deleted successfully";
    }

    @GetMapping("/flood/stations")
    public List<FloodStationResponse> getFloodStations(@RequestParam List<Integer> stations) {
        logger.info("GET /flood/stations - Fetching flood stations for: {}", stations);
        List<FloodStationResponse> responses = fireStationService.getFloodStations(stations);
        logger.info("list of all homes served by the station: {}", responses);
        return responses;
    }

    @GetMapping("/fire")
    public FireResponse getResidentsByAddress(@RequestParam String address) {
        logger.info("GET /fire - Fetching residents for address: {}", address);
        FireResponse response = fireStationService.getResidentsByAddress(address);
        logger.info("the list of residents living at the address: {}", response);
        return response;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFireStation(@RequestParam("firestation") int stationNumber) {
        logger.info("GET /phoneAlert?firestation={} - Fetching phone numbers", stationNumber);
        List<String> phoneNumbers = fireStationService.getPhoneNumbersByFireStation(stationNumber);
        logger.info("Phone: {}", phoneNumbers);
        return phoneNumbers;
    }

    @GetMapping("/childAlert")
    public List<ChildAlertResponse> getChildAlert(@RequestParam("address") String address) {
        logger.info("GET /childAlert?address={} - Fetching child alerts", address);
        List<ChildAlertResponse> childAlerts = fireStationService.getChildAlert(address);
        logger.info("ChildAlerts: {}", childAlerts);
        return childAlerts;
    }

    @GetMapping("/firestation")
    public FireStationResponse getPersonsCoveredByStation(@RequestParam("stationNumber") int stationNumber) {
        logger.info("GET /firestation?stationNumber={} - Fetching persons covered by station", stationNumber);
        FireStationResponse response = fireStationService.getPersonsByStationNumber(stationNumber);
        logger.info("list of people covered by the corresponding fire station: {}", response);
        return response;
    }
}
