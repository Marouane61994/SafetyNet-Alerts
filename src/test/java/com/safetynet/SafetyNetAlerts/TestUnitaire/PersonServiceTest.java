package com.safetynet.SafetyNetAlerts.TestUnitaire;



import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.Response.PersonInfo;
import com.safetynet.SafetyNetAlerts.Service.PersonService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;



@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;



    @Test
    void testGetAllPersons() {
        when(personRepository.findAll()).thenReturn(List.of(new PersonModel()));

        List<PersonModel> persons = personService.getAllPersons();

        assertEquals(1, persons.size());
    }

    @Test
    void testAddPerson() {
        personService.addPerson(new PersonModel());
        verify(personRepository, times(1)).save(new PersonModel());
    }

    @Test
    void testUpdatePerson() {
        when(personRepository.findByFullName("John", "Boyd")).thenReturn(Optional.of(new PersonModel()));

        PersonModel updatedPerson = new PersonModel();

        PersonModel result = personService.updatePerson("John", "Boyd", updatedPerson);

        assertEquals("1509 Culver St", result.getAddress());
        assertEquals("Culver", result.getCity());
        assertEquals("97451", result.getZip());
    }

    @Test
    void testDeletePerson() {
        personService.deletePerson("John", "Boyd");
        verify(personRepository, times(1)).deleteByFullName("John", "Boyd");
    }

    @Test
    void testGetCommunityEmailsByCity() {
        when(personRepository.findAll()).thenReturn(List.of(new PersonModel()));

        List<String> emails = personService.getCommunityEmailsByCity("Culver");

        assertEquals(1, emails.size());
        assertEquals("jaboyd@email.com", emails.get(0));
    }

    @Test
    void testGetPersonInfoByLastName() {
        when(personRepository.findByLastName("Boyd")).thenReturn(List.of(new PersonModel()));
        when(personRepository.findByFullName("Tenley", "Boyd")).thenReturn(Optional.of(new PersonModel()));

        List<PersonInfo> infos = personService.getPersonInfoByLastName("Boyd");

        assertEquals(1, infos.size());
        assertEquals("Tenley", infos.get(0).getFirstName());
        assertEquals("Boyd", infos.get(0).getLastName());

    }


}


