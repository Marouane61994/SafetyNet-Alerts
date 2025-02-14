package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Response.PersonInfo;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing persons in the SafetyNet Alerts application.
 * This class handles CRUD operations and specific queries related to persons.
 */
@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Retrieves a list of all registered persons.
     *
     * @return a list of {@link PersonModel} objects representing all persons.
     */
    public List<PersonModel> getAllPersons() {
        return personRepository.findAll();
    }

    /**
     * Adds a new person to the database.
     *
     * @param persons the {@link PersonModel} object representing the person to be added.
     */
    public void addPerson(PersonModel persons) {
        personRepository.save(persons);
    }

    /**
     * Updates the information of an existing person.
     *
     * @param firstName      the first name of the person to update.
     * @param lastName       the last name of the person to update.
     * @param updatedPerson  the {@link PersonModel} object containing the updated information.
     * @return the updated {@link PersonModel} object.
     * @throws RuntimeException if the person is not found.
     */
    public PersonModel updatePerson(String firstName, String lastName, PersonModel updatedPerson) {
        PersonModel existingPerson = personRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("Person not found with name: " + firstName + " " + lastName));

        existingPerson.setAddress(updatedPerson.getAddress());
        existingPerson.setCity(updatedPerson.getCity());
        existingPerson.setZip(updatedPerson.getZip());
        existingPerson.setPhone(updatedPerson.getPhone());
        existingPerson.setEmail(updatedPerson.getEmail());

        return personRepository.save(existingPerson);
    }

    /**
     * Deletes a person from the database based on their first and last name.
     *
     * @param firstName the first name of the person to delete.
     * @param lastName  the last name of the person to delete.
     */
    public void deletePerson(String firstName, String lastName) {
        personRepository.deleteByFullName(firstName, lastName);
    }

    /**
     * Retrieves all email addresses of residents in a given city.
     *
     * @param city the name of the city for which to retrieve email addresses.
     * @return a list of unique email addresses of residents.
     */
    public List<String> getCommunityEmailsByCity(String city) {
        return personRepository.findAll().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(PersonModel::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves detailed information about persons with a given last name.
     * Includes address, age, email, medications, and allergies.
     *
     * @param lastName the last name of the persons to retrieve.
     * @return a list of {@link PersonInfo} objects containing personal details.
     */
    public List<PersonInfo> getPersonInfoByLastName(String lastName) {

        List<PersonModel> persons = personRepository.findByLastName(lastName);

        return persons.stream().map(person -> {
            MedicalRecordModel medicalRecord = medicalRecordRepository.findByFullName(person.getFirstName(), person.getLastName())
                    .orElse(null);

            int age = (medicalRecord != null) ? calculateAge(medicalRecord.getBirthdate()) : -1;

            return new PersonInfo(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    age,
                    person.getEmail(),
                    medicalRecord != null ? medicalRecord.getMedications() : List.of(),
                    medicalRecord != null ? medicalRecord.getAllergies() : List.of()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Calculates the age of a person based on their birthdate.
     *
     * @param birthDate the birthdate of the person.
     * @return the person's age in years.
     */
    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}