package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;


    @GetMapping
    public List<PersonModel> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping
    public void addPerson(@RequestBody PersonModel person) {
        personService.addPerson(person);
    }

    @PutMapping("/{firstName}/{lastName}")
    public PersonModel updatePerson(@RequestBody PersonModel person,@PathVariable String firstName, @PathVariable String lastName) {
        return personService.updatePerson(firstName, lastName,person);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public String deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        personService.deletePerson(firstName, lastName);
        return "Person deleted successfully";
    }
}

