package com.safetynet.SafetyNetAlerts.Controller;


import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Service.PersonService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Data
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public List<PersonModel> getAllPersons() {
        return personService.findAll();
    }

    @PostMapping
    public void addPerson(@RequestBody PersonModel person) {
        personService.save(person);
    }

    @DeleteMapping
    public void deletePerson(@RequestBody PersonModel person) {
        personService.delete(person);
    }
}
