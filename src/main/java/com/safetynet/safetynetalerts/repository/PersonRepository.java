package com.safetynet.safetynetalerts.repository;


import com.safetynet.safetynetalerts.model.PersonModel;
import com.safetynet.safetynetalerts.service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PersonRepository {

    private final DataLoaderService dataLoaderService;

    public List<PersonModel> findAll() {
        return dataLoaderService.getDataModel().getPersons();
    }

    public Optional<PersonModel> findByFullName(String firstName, String lastName) {
        return dataLoaderService.getDataModel().getPersons()
                .stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

    }

    public PersonModel save(PersonModel person) {
        List<PersonModel> persons = dataLoaderService.getDataModel().getPersons();
        persons.removeIf(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName())
                && p.getLastName().equalsIgnoreCase(person.getLastName()));

        persons.add(person);
        dataLoaderService.getDataModel().setPersons(persons);
        dataLoaderService.writeJsonToFile();
        return person;
    }

    public void deleteByFullName(String firstName, String lastName) {
        List<PersonModel> persons = dataLoaderService.getDataModel().getPersons();
        persons.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
                && person.getLastName().equalsIgnoreCase(lastName));
        dataLoaderService.getDataModel().setPersons(persons);
        dataLoaderService.writeJsonToFile();
    }

    public List<PersonModel> findByLastName(String lastName) {
        return findAll().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }
}










