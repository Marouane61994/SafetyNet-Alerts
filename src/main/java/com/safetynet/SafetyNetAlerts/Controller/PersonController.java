package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Model.PersonInfo;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Service.PersonService;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PersonController {

    private final PersonService personService;


    @GetMapping("/person")
    public List<PersonModel> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping("/person")
    public PersonModel addPerson(@RequestBody PersonModel person) {
        personService.addPerson(person);
        return person;
    }

    @PutMapping("/person/{firstName}/{lastName}")
    public PersonModel updatePerson(@RequestBody PersonModel person, @PathVariable String firstName, @PathVariable String lastName) {
        return personService.updatePerson(firstName, lastName, person);
    }

    @DeleteMapping("/person/{firstName}/{lastName}")
    public String deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        personService.deletePerson(firstName, lastName);
        return "Person deleted successfully";
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
        return personService.getCommunityEmailsByCity(city);
    }

    @GetMapping("/personInfolastName")
    public List<PersonInfo> getPersonInfoByLastName(@RequestParam String lastName) {
        return personService.getPersonInfoByLastName(lastName);
    }

}



