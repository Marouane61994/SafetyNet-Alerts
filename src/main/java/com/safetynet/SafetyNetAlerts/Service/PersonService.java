package com.safetynet.SafetyNetAlerts.Service;

import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;

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
}


