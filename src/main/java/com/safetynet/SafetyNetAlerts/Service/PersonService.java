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

    public List<PersonModel> findAll() {
        return personRepository.findAll();
    }

    public void save(PersonModel person) {
        personRepository.save(person);
    }

    public void delete(PersonModel person) {
        personRepository.delete(person);
    }
    public void update(PersonModel person) {
        personRepository.update(person);
    }
}

