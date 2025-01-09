package com.safetynet.SafetyNetAlerts.Controller;


import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

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

    @PutMapping
    public void updatePerson(@RequestBody PersonModel person) {
        personService.update(person);
    }
}
