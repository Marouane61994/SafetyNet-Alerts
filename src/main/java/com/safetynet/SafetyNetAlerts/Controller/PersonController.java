package com.safetynet.SafetyNetAlerts.Controller;

import com.safetynet.SafetyNetAlerts.Response.PersonInfo;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    @GetMapping("/person")
    public List<PersonModel> getAllPersons() {
        logger.info("GET /person - Fetching all persons");
        List<PersonModel> persons = personService.getAllPersons();
        logger.info("Persons: {}", persons);
        return persons;
    }

    @PostMapping("/person")
    public PersonModel addPerson(@RequestBody PersonModel person) {
        logger.info("POST /person - Adding new person: {}", person);
        personService.addPerson(person);
        logger.info("Person added successfully: {}", person);
        return person;
    }

    @PutMapping("/person/{firstName}/{lastName}")
    public PersonModel updatePerson(@RequestBody PersonModel person, @PathVariable String firstName, @PathVariable String lastName) {
        logger.info("PUT /person/{}/{} - Updating person with new data: {}", firstName, lastName, person);
        PersonModel updatedPerson = personService.updatePerson(firstName, lastName, person);
        logger.info("Updated person: {}", updatedPerson);
        return updatedPerson;
    }

    @DeleteMapping("/person/{firstName}/{lastName}")
    public String deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("DELETE /person/{}/{} - Deleting person", firstName, lastName);
        personService.deletePerson(firstName, lastName);
        logger.info("Person deleted successfully: {} {}", firstName, lastName);
        return "Person deleted successfully";
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
        logger.info("GET /communityEmail?city={} - Fetching community emails", city);
        List<String> emails = personService.getCommunityEmailsByCity(city);
        logger.info("Email: {}", emails);
        return emails;
    }

    @GetMapping("/personInfolastName")
    public List<PersonInfo> getPersonInfoByLastName(@RequestParam String lastName) {
       logger.info("GET /personInfolastName?lastName={} - Fetching person info", lastName);
        List<PersonInfo> personInfoList = personService.getPersonInfoByLastName(lastName);
       logger.info("Response: {}", personInfoList);
        return personInfoList;
    }

}




