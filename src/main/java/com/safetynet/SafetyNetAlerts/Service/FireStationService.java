package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.*;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.Response.ChildAlertResponse;
import com.safetynet.SafetyNetAlerts.Response.FireResponse;
import com.safetynet.SafetyNetAlerts.Response.FireStationResponse;
import com.safetynet.SafetyNetAlerts.Response.FloodStationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;
    private final DataLoaderService dataLoaderService;
    private final PersonRepository personRepository;
    private final PersonService personService;


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
                            var medicalRecord = getMedicalRecordModel(person, dataModel);

                            int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);

                            return new FloodStationResponse.ResidentInfo(
                                    person.getFirstName(),
                                    person.getLastName(),
                                    person.getPhone(),
                                    age,
                                    medicalRecord.map(MedicalRecordModel::getMedications).orElse(Collections.emptyList()),
                                    medicalRecord.map(MedicalRecordModel::getAllergies).orElse(Collections.emptyList())
                            );
                        }, Collectors.toList())
                ));

        // Construire la liste des réponses
        return addressToResidents.entrySet().stream()
                .map(entry -> new FloodStationResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public FireResponse getResidentsByAddress(String address) {
        var dataModel = dataLoaderService.getDataModel();

        // Récupérer le numéro de caserne pour l'adresse
        int stationNumber = Integer.parseInt(dataModel.getFireStation().stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .map(FireStationModel::getStation)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Adresse non desservie par une caserne")));

        // Récupérer les habitants de l'adresse
        List<FloodStationResponse.ResidentInfo> residents = getResidentInfos(address, dataModel);

        return new FireResponse(address, stationNumber, residents);
    }

    private List<FloodStationResponse.ResidentInfo> getResidentInfos(String address, DataModel dataModel) {

        return dataModel.getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .map(person -> {
                    // Récupérer les antécédents médicaux
                    var medicalRecord = getMedicalRecordModel(person, dataModel);

                    // Calculer l'âge
                    int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);

                    var residentInfo = new FloodStationResponse.ResidentInfo();
                    residentInfo.setFirstName(person.getFirstName());
                    residentInfo.setLastName(person.getLastName());
                    residentInfo.setPhone(person.getPhone());
                    residentInfo.setAge(age);
                    residentInfo.setMedications(medicalRecord.map(MedicalRecordModel::getMedications).orElse(Collections.emptyList()));
                    residentInfo.setAllergies(medicalRecord.map(MedicalRecordModel::getAllergies).orElse(Collections.emptyList()));
                    return residentInfo;

                })
                .collect(Collectors.toList());
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
                    var medicalRecord = getMedicalRecordModel(person, dataModel);

                    int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);

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

    public FireStationResponse getPersonsByStationNumber(int stationNumber) {
        // Charger les données
        var dataModel = dataLoaderService.getDataModel();

        // Récupérer les adresses couvertes par la station
        Set<String> addresses = dataModel.getFireStation().stream()
                .filter(fireStation -> Integer.parseInt(fireStation.getStation()) == stationNumber)
                .map(FireStationModel::getAddress)
                .collect(Collectors.toSet());

        // Récupérer les personnes habitant ces adresses
        List<FireStationResponse.PersonInfo> persons = dataModel.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(person -> {
                    var personInfo = new FireStationResponse.PersonInfo();
                    personInfo.setFirstName(person.getFirstName());
                    personInfo.setLastName(person.getLastName());
                    personInfo.setAddress(person.getAddress());
                    personInfo.setPhone(person.getPhone());
                    return personInfo;
                })
                .collect(Collectors.toList());

        // Calculer le nombre d'adultes et d'enfants
        int adults = 0;
        int children = 0;
        for (PersonModel person : dataModel.getPersons()) {
            if (addresses.contains(person.getAddress())) {
                var medicalRecord = getMedicalRecordModel(person, dataModel);

                if (medicalRecord.isPresent()) {
                    int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);
                    if (age <= 18) {
                        children++;
                    } else {
                        adults++;
                    }
                }
            }
        }
        // Construire la réponse
        FireStationResponse response = new FireStationResponse();
        response.setPersons(persons);
        response.setAdultCount(adults);
        response.setChildCount(children);

        return response;
    }

    private static Optional<MedicalRecordModel> getMedicalRecordModel(PersonModel person, DataModel dataModel) {
        return dataModel.getMedicalRecord().stream()
                .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                        record.getLastName().equals(person.getLastName()))
                .findFirst();
    }


}









