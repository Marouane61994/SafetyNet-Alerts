package com.safetynet.SafetyNetAlerts.Repository;


import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Data
@Repository
public class PersonRepository {
    @Autowired
    private DataLoaderService dataLoaderService;

    public List<PersonModel> findAll() {
        return dataLoaderService.getPersons();
    }

    public void save(PersonModel person) {
        dataLoaderService.getPersons().add(person);
    }

    public void delete(PersonModel person) {
        dataLoaderService.getPersons().remove(person);
    }
}









