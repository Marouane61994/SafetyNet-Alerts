package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Model.PersonInfo;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
@Data
@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;


    public List<PersonModel> getAllPersons() {
        return personRepository.findAll();
    }

    public void addPerson(PersonModel persons) {
        personRepository.save(persons);
    }

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

    public void deletePerson(String firstName, String lastName) {
        personRepository.deleteByFullName(firstName, lastName);
    }

    public List<String> getCommunityEmailsByCity(String city) {
        return personRepository.findAll().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(PersonModel::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }
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

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}



