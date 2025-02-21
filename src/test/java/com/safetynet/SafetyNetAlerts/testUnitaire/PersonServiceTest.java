package com.safetynet.SafetyNetAlerts.testUnitaire;

import com.safetynet.SafetyNetAlerts.model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.model.PersonModel;
import com.safetynet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.response.PersonInfo;
import com.safetynet.SafetyNetAlerts.service.PersonService;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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


    @Test
    void TestAllPersons() {
        var person = new PersonModel();
        person.setLastName("John");
        person.setFirstName("Boyd");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");
        person.setCity("Culver");
        person.setZip("97451");
        person.setAddress("1509 Culver St");
        when(personRepository.findAll()).thenReturn(List.of(person));

        List<PersonModel> result = personService.getAllPersons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertThat(person).isEqualTo(result.get(0)).usingRecursiveComparison();
    }

    @Test
    void TestAddPerson() {
        var person = new PersonModel();
        person.setLastName("John");
        person.setFirstName("Boyd");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");
        person.setCity("Culver");
        person.setZip("97451");
        person.setAddress("1509 Culver St");
        personService.addPerson(person);

        verify(personRepository, times(1)).save(person);
    }

    @Test
    void TestUpdatePerson() {
        var person = new PersonModel();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");
        person.setCity("Culver");
        person.setZip("97451");
        person.setAddress("1509 Culver St");
        when(personRepository.findByFullName("John", "Boyd")).thenReturn(Optional.of(person));

        PersonModel updatedPerson = new PersonModel();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Boyd");
        updatedPerson.setAddress("1509 Culver St");
        updatedPerson.setCity("Culver");
        updatedPerson.setZip("97451");
        updatedPerson.setPhone("841-874-6512");
        updatedPerson.setEmail("jaboyd@email.com");
        when(personRepository.save(updatedPerson)).thenReturn(updatedPerson);
        PersonModel result = personService.updatePerson("John", "Boyd", updatedPerson);

        assertEquals("1509 Culver St", result.getAddress());
        assertEquals("Culver", result.getCity());
        assertEquals("97451", result.getZip());
        assertEquals("841-874-6512", result.getPhone());
        assertEquals("jaboyd@email.com", result.getEmail());

        verify(personRepository, times(1)).save(any(PersonModel.class));
    }

    @Test
    void TestDeletePerson() {
        var person = new PersonModel();
        person.setLastName("John");
        person.setFirstName("Boyd");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");
        person.setCity("Culver");
        person.setZip("97451");
        person.setAddress("1509 Culver St");
        personService.deletePerson("John", "Boyd");

        verify(personRepository, times(1)).deleteByFullName("John", "Boyd");
    }

    @Test
    void TestReturnEmailsByCity() {
        var person = new PersonModel();
        person.setLastName("John");
        person.setFirstName("Boyd");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");
        person.setCity("Culver");
        person.setZip("97451");
        person.setAddress("1509 Culver St");
        when(personRepository.findAll()).thenReturn(List.of(person));

        List<String> emails = personService.getCommunityEmailsByCity("Culver");

        assertEquals(1, emails.size());
        assertEquals("jaboyd@email.com", emails.get(0));
    }

    @Test
    void TestGetPersonInfoByLastName() {

        var medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setFirstName("Boyd");
        medicalRecord.setBirthdate(LocalDate.parse("1984-06-03"));
        medicalRecord.setMedications(List.of("aznol:350mg",
                "hydrapermazol:100mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        var person = new PersonModel();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");
        person.setCity("Culver");
        person.setZip("97451");
        person.setAddress("1509 Culver St");

        when(personRepository.findByLastName("Boyd")).thenReturn(List.of(person));
        when(medicalRecordRepository.findByFullName("John", "Boyd")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> result = personService.getPersonInfoByLastName("Boyd");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Boyd", result.get(0).getLastName());
        assertEquals(40, result.get(0).getAge());
        assertEquals("jaboyd@email.com", result.get(0).getEmail());
        assertThat(result.get(0).getMedications()).containsExactlyInAnyOrder("aznol:350mg", "hydrapermazol:100mg");
        assertThat(result.get(0).getAllergies()).containsExactlyInAnyOrder("nillacilan");
    }

    @Test
    void TestCalculateCorrectAge() {
        LocalDate birthDate = LocalDate.of(2000, 2, 1);
        int age = personService.calculateAge(birthDate);

        assertEquals(25, age);
    }
}






