package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.*;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
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
    private final PersonRepository personRepository;

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
                    response.setPersons(entry.getValue());
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


    public List<String> getPhoneNumbersByFireStation(int stationNumber) {
        // Récupérer les adresses couvertes par la caserne
        List<String> coveredAddresses = fireStationRepository.findAddressesByStationNumber(String.valueOf(stationNumber));

        // Filtrer les personnes par adresse et récupérer leurs numéros
        return personRepository.findAll().stream()
                .filter(person -> coveredAddresses.contains(person.getAddress())) // Vérifie si l'adresse est couverte
                .map(PersonModel::getPhone) // Récupère les numéros de téléphone
                .distinct() // Élimine les doublons
                .collect(Collectors.toList());
    }

    public List<ChildAlertResponse> getChildAlert(String address) {
        // Charger les données depuis le DataModel
        var dataModel = dataLoaderService.getDataModel();

        // Trouver les résidents vivant à l'adresse spécifiée
        List<PersonModel> residents = dataModel.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        // Séparer les enfants (<= 18 ans) des autres résidents
        List<ChildAlertResponse.ChildInfo> children = residents.stream()
                .map(person -> {
                    var medicalRecord = dataModel.getMedicalRecord().stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName())
                                    && record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    int age = (medicalRecord != null) ? calculateAge(medicalRecord.getBirthdate()) : -1;

                    // Retourner uniquement si la personne est un enfant
                    if (age <= 18) {
                        return new ChildAlertResponse.ChildInfo(
                                person.getFirstName(),
                                person.getLastName(),
                                age
                        );

                    }
                    return null;
                })
                .filter(Objects::nonNull) // Supprimer les valeurs null
                .toList();

        // Si aucun enfant n'est trouvé, retourner une liste vide
        if (children.isEmpty()) {
            return Collections.emptyList();
        }

        // Ajouter les autres résidents comme membres du foyer
        List<ChildAlertResponse.FamilyMemberInfo> familyMembers = residents.stream()
                .filter(person -> children.stream()
                        .noneMatch(child -> child.getFirstName().equals(person.getFirstName())
                                && child.getLastName().equals(person.getLastName())))
                .map(person -> new ChildAlertResponse.FamilyMemberInfo(
                        person.getFirstName(),
                        person.getLastName()
                ))
                .collect(Collectors.toList());

        // Construire la réponse pour chaque enfant
        return children.stream()
                .map(child -> new ChildAlertResponse(
                        child.getFirstName(),
                        child.getLastName(),
                        child.getAge(),
                        familyMembers
                ))
                .collect(Collectors.toList());
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


}










