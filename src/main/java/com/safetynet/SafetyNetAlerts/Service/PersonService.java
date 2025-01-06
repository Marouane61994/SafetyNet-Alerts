package com.safetynet.SafetyNetAlerts.Service;


import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public List<PersonModel> findAll() {
        return personRepository.findAll();
    }

    public void save(PersonModel person) {
        personRepository.save(person);
    }

    public void delete(PersonModel person) {
        personRepository.delete(person);
    }
}

