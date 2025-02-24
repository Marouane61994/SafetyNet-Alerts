package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.*;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing fire stations in the SafetyNet Alerts application.
 * Provides methods to retrieve and manipulate fire station data, including related persons and emergency responses.
 */
@RequiredArgsConstructor
@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;
    private final DataLoaderService dataLoaderService;
    private final PersonRepository personRepository;
    private final PersonService personService;

    /**
     * Retrieves a list of all fire stations.
     *
     * @return a list of {@link FireStationModel} objects representing all fire stations.
     */
    public List<FireStationModel> getAllFireStations() {
        return fireStationRepository.findAll();
    }

    /**
     * Adds a new fire station.
     *
     * @param fireStation the {@link FireStationModel} object representing the fire station to be added.
     * @return the added fire station.
     */
    public FireStationModel addFireStation(FireStationModel fireStation) {
        return fireStationRepository.save(fireStation);
    }

    /**
     * Updates an existing fire station by its address.
     *
     * @param address           the address of the fire station to update.
     * @param updatedFireStation the updated {@link FireStationModel} object.
     * @return the updated fire station.
     * @throws RuntimeException if the fire station is not found.
     */
    public FireStationModel updateFireStation(String address, FireStationModel updatedFireStation) {
        FireStationModel existingFireStation = fireStationRepository.findByAddress(address)
                .orElseThrow(() -> new RuntimeException("FireStation not found with address: " + address));

        existingFireStation.setStation(updatedFireStation.getStation());

        return fireStationRepository.save(existingFireStation);
    }

    /**
     * Deletes a fire station by its address.
     *
     * @param address the address of the fire station to delete.
     */
    public void deleteFireStation(String address) {
        fireStationRepository.deleteByAddress(address);
    }

    /**
     * Retrieves a list of residents covered by multiple fire stations (flood stations).
     *
     * @param stationNumbers a list of station numbers to fetch residents from.
     * @return a list of {@link FloodStationResponse} objects containing residents by station.
     */
    public List<FloodStationResponse> getFloodStations(List<Integer> stationNumbers) {
        // Charger les données
        var dataModel = dataLoaderService.getDataModel();
        Set<String> stationNumbersAsString = stationNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());


        Set<String> addresses = dataModel.getFireStation().stream()
                .filter(fireStation -> stationNumbersAsString.contains(fireStation.getStation()))
                .map(FireStationModel::getAddress)
                .collect(Collectors.toSet());


        Map<String, List<ResidentInfo>> addressToResidents = dataModel.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(
                        PersonModel::getAddress,
                        Collectors.mapping(person -> {
                            var medicalRecord = getMedicalRecordModel(person, dataModel);

                            int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);

                            return new ResidentInfo(
                                    person.getFirstName(),
                                    person.getLastName(),
                                    person.getPhone(),
                                    age,
                                    medicalRecord.map(MedicalRecordModel::getMedications).orElse(Collections.emptyList()),
                                    medicalRecord.map(MedicalRecordModel::getAllergies).orElse(Collections.emptyList())
                            );
                        }, Collectors.toList())
                ));


        return addressToResidents.entrySet().stream()
                .map(entry -> new FloodStationResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves fire station details and residents for a given address.
     *
     * @param address the address to search for.
     * @return a {@link FireResponse} containing fire station number and resident details.
     */
    public FireResponse getResidentsByAddress(String address) {
        var dataModel = dataLoaderService.getDataModel();


        int stationNumber = Integer.parseInt(dataModel.getFireStation().stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .map(FireStationModel::getStation)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Adresse non desservie par une caserne")));


        List<ResidentInfo> residents = getResidentInfos(address, dataModel);

        return new FireResponse(address, stationNumber, residents);
    }
    private List<ResidentInfo> getResidentInfos(String address, DataModel dataModel) {

        return dataModel.getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .map(person -> {
                    // Récupérer les antécédents médicaux
                    var medicalRecord = getMedicalRecordModel(person, dataModel);

                    // Calculer l'âge
                    int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);

                    var residentInfo = new ResidentInfo();
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

    /**
     * Retrieves all phone numbers of residents covered by a specific fire station.
     *
     * @param stationNumber the fire station number.
     * @return a list of unique phone numbers.
     */
    public List<String> getPhoneNumbersByFireStation(int stationNumber) {

        List<String> coveredAddresses = fireStationRepository.findAddressesByStationNumber(String.valueOf(stationNumber));


        return personRepository.findAll().stream()
                .filter(person -> coveredAddresses.contains(person.getAddress())) // Vérifie si l'adresse est couverte
                .map(PersonModel::getPhone) // Récupère les numéros de téléphone
                .distinct() // Élimine les doublons
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of children and their family members living at a given address.
     *
     * @param address the address to search for.
     * @return a list of {@link ChildAlertResponse} objects containing children and their family members.
     */
    public List<ChildAlertResponse> getChildAlert(String address) {

        var dataModel = dataLoaderService.getDataModel();


        List<PersonModel> residents = dataModel.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        List<ChildInfo> children = residents.stream()
                .map(person -> {
                    var medicalRecord = getMedicalRecordModel(person, dataModel);

                    int age = medicalRecord.map(record -> personService.calculateAge(record.getBirthdate())).orElse(-1);


                    if (age <= 18) {
                        return new ChildInfo(
                                person.getFirstName(),
                                person.getLastName(),
                                age
                        );

                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();


        if (children.isEmpty()) {
            return Collections.emptyList();
        }

        List<FamilyMemberInfo> familyMembers = residents.stream()
                .filter(person -> children.stream()
                        .noneMatch(child -> child.getFirstName().equals(person.getFirstName())
                                && child.getLastName().equals(person.getLastName())))
                .map(person -> new FamilyMemberInfo(
                        person.getFirstName(),
                        person.getLastName()
                ))
                .collect(Collectors.toList());

        return children.stream()
                .map(child -> new ChildAlertResponse(
                        child.getFirstName(),
                        child.getLastName(),
                        child.getAge(),
                        familyMembers
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of persons covered by a specific fire station.
     *
     * @param stationNumber the fire station number.
     * @return a {@link FireStationResponse} containing person details and a count of adults and children.
     */
    public FireStationResponse getPersonsByStationNumber(int stationNumber) {
        var dataModel = dataLoaderService.getDataModel();

        Set<String> addresses = dataModel.getFireStation().stream()
                .filter(fireStation -> Integer.parseInt(fireStation.getStation()) == stationNumber)
                .map(FireStationModel::getAddress)
                .collect(Collectors.toSet());

        List<FireStationInfo> persons = dataModel.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(person -> {
                    var fireStationInfo = new FireStationInfo();
                    fireStationInfo.setFirstName(person.getFirstName());
                    fireStationInfo.setLastName(person.getLastName());
                    fireStationInfo.setAddress(person.getAddress());
                    fireStationInfo.setPhone(person.getPhone());
                    return fireStationInfo;
                })
                .collect(Collectors.toList());

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