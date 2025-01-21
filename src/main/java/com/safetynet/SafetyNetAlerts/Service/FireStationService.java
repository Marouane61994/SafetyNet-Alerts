package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.*;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;
    private final DataLoaderService dataLoaderService;
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

    public FireResponse getResidentsByAddress(String address) {
        DataModel data = dataLoaderService.getDataModel();

        // Récupérer le numéro de caserne pour l'adresse
        int stationNumber = Integer.parseInt(data.getFireStation().stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .map(FireStationModel::getStation)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Adresse non desservie par une caserne")));

        // Récupérer les habitants de l'adresse
        List<FloodStationResponse.ResidentInfo> residents = data.getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .map(person -> {
                    // Récupérer les antécédents médicaux
                    MedicalRecordModel medicalRecord = data.getMedicalRecord().stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName())
                                    && record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    // Calculer l'âge
                    int age = (medicalRecord != null && medicalRecord.getBirthdate() != null)
                            ? calculateAge(medicalRecord.getBirthdate())
                            : -1;

                    var residentInfo = new FloodStationResponse.ResidentInfo();
                    residentInfo.setFirstName(person.getFirstName());
                    residentInfo.setLastName(person.getLastName());
                    residentInfo.setPhone(person.getPhone());
                    residentInfo.setAge(age);
                    residentInfo.setMedications(medicalRecord != null ? medicalRecord.getMedications() : Collections.emptyList());
                    residentInfo.setAllergies(medicalRecord != null ? medicalRecord.getAllergies() : Collections.emptyList());
                    return residentInfo;

                })
                .collect(Collectors.toList());

        return new FireResponse(address, stationNumber, residents);
    }



    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}










