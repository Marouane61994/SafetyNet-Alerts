package com.safetynet.SafetyNetAlerts.TestUnitaire;

import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.Response.PersonInfo;
import com.safetynet.SafetyNetAlerts.Service.PersonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private PersonService personService;

    private PersonModel person;
    private final MedicalRecordModel medicalRecord = new MedicalRecordModel();

    @BeforeEach
    void setUp() {
        person = new PersonModel();
    }

    @Test
    void testGetAllPersons() {
        when(personRepository.findAll()).thenReturn(List.of(person));

        List<PersonModel> result = personService.getAllPersons();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void testAddPerson() {
        when(personRepository.save(any(PersonModel.class))).thenReturn(person);

        personService.addPerson(person);

        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testUpdatePerson() {
        when(personRepository.findByFullName("John", "Doe")).thenReturn(Optional.of(person));
        when(personRepository.save(any(PersonModel.class))).thenReturn(person);

        PersonModel updatedPerson = new PersonModel();
        PersonModel result = personService.updatePerson("John", "Doe", updatedPerson);

        assertNotNull(result);
        assertEquals("456 Elm St", result.getAddress());
        verify(personRepository, times(1)).save(any(PersonModel.class));
    }

    @Test
    void testDeletePerson() {
        doNothing().when(personRepository).deleteByFullName("John", "Doe");

        personService.deletePerson("John", "Doe");

        verify(personRepository, times(1)).deleteByFullName("John", "Doe");
    }

    @Test
    void testGetCommunityEmailsByCity() {
        when(personRepository.findAll()).thenReturn(List.of(person));

        List<String> result = personService.getCommunityEmailsByCity("Springfield");

        assertEquals(1, result.size());
        assertEquals("john.doe@email.com", result.get(0));
    }

    @Test
    void testGetPersonInfoByLastName() {
        when(personRepository.findByLastName("Doe")).thenReturn(List.of(person));
        when(medicalRecordRepository.findByFullName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> result = personService.getPersonInfoByLastName("Doe");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals(34, result.get(0).getAge());
    }


}




