package com.safetynet.SafetyNetAlerts.Repository;

import com.safetynet.SafetyNetAlerts.Model.*;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.Period;
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
    public List<FloodStationResponse> getFloodStations(List<Integer> stationNumbers) {
        // Charger les données
        var dataModel = dataLoaderService.getDataModel();
        Set<String> stationNumbersAsString = stationNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());

        // Trouver les adresses associées aux stations
        Set<String> addresses = dataModel.getFireStation().stream()
                .filter(fireStation -> stationNumbersAsString.contains(fireStation.getStation()))
                .map(FireStationModel::getAddress)
                .collect(Collectors.toSet());

        // Regrouper les habitants par adresse
        Map<String, List<FloodStationResponse.ResidentInfo>> addressToResidents = dataModel.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(
                        PersonModel::getAddress,
                        Collectors.mapping(person -> {
                            var medicalRecord = dataModel.getMedicalRecord().stream()
                                    .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                                            record.getLastName().equals(person.getLastName()))
                                    .findFirst()
                                    .orElse(null);

                            int age = (medicalRecord != null) ? calculateAge(medicalRecord.getBirthdate()) : -1;

                            var residentInfo = new FloodStationResponse.ResidentInfo();
                            residentInfo.setFirstName(person.getFirstName());
                            residentInfo.setLastName(person.getLastName());
                            residentInfo.setPhone(person.getPhone());
                            residentInfo.setAge(age);
                            residentInfo.setMedications(medicalRecord != null ? medicalRecord.getMedications() : Collections.emptyList());
                            residentInfo.setAllergies(medicalRecord != null ? medicalRecord.getAllergies() : Collections.emptyList());
                            return residentInfo;
                        }, Collectors.toList())
                ));

        // Construire la liste des réponses
        return addressToResidents.entrySet().stream()
                .map(entry -> {
                    var response = new FloodStationResponse();
                    response.setAddress(entry.getKey());
                    response.setPerson(entry.getValue());
                    return response;
                })
                .collect(Collectors.toList());
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}

