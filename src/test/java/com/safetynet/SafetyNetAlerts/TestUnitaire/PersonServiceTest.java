package com.safetynet.SafetyNetAlerts.TestUnitaire;



import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.Service.PersonService;
import lombok.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Data
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPersons() {

        List<PersonModel> mockPersons = Arrays.asList(
                new PersonModel(),
                new PersonModel()
        );
        when(personRepository.findAll()).thenReturn(mockPersons);

        List<PersonModel> result = personService.getAllPersons();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void testAddPerson() {
        PersonModel newPerson = new PersonModel();

        personService.addPerson(newPerson);

        verify(personRepository, times(1)).save(newPerson);
    }


    @Test
    void testUpdatePerson_NotFound() {
        String firstName = "NonExistent";
        String lastName = "Person";
        PersonModel updatedPerson = new PersonModel();

        when(personRepository.findByFullName(firstName, lastName)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> personService.updatePerson(firstName, lastName, updatedPerson));
        assertThat(exception.getMessage()).isEqualTo("Person not found with name: NonExistent Person");
        verify(personRepository, times(1)).findByFullName(firstName, lastName);
        verify(personRepository, never()).save(any());
    }

    @Test
    void testDeletePerson() {

        String firstName = "John";
        String lastName = "Doe";


        personService.deletePerson(firstName, lastName);


        verify(personRepository, times(1)).deleteByFullName(firstName, lastName);
    }


}
