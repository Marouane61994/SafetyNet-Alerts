package com.safetynet.SafetyNetAlerts.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNetAlerts.Model.DataModel;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Repository
@Getter
@Data
public class DataRepository {
    private DataModel dataModel;
    private List<PersonModel> persons;

    public DataRepository() {
        this.persons = loadPersonsFromJson();
    }

    private List<PersonModel> loadPersonsFromJson() {
        // Logique de chargement des données depuis le JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream("/data.json")) {
           dataModel  = objectMapper.readValue(inputStream, DataModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(); // Retourne une liste vide en cas d'erreur
    }

        public void addPerson(PersonModel person) {
            persons.add(person);
        }

        public void updatePerson(PersonModel person) {
            persons.removeIf(p -> p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName()));
            persons.add(person);
        }

        public void deletePerson(String firstName, String lastName) {
            persons.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));
        }

        public List<PersonModel> getPersons() {
            return persons;
        }
    }

