package com.safetynet.SafetyNetAlerts.TestUnitaire;

import com.safetynet.SafetyNetAlerts.Model.DataModel;
import com.safetynet.SafetyNetAlerts.Model.FireStationModel;
import com.safetynet.SafetyNetAlerts.Model.MedicalRecordModel;
import com.safetynet.SafetyNetAlerts.Model.PersonModel;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.Response.FireStationResponse;
import com.safetynet.SafetyNetAlerts.Response.FloodStationResponse;
import com.safetynet.SafetyNetAlerts.Response.FireResponse;
import com.safetynet.SafetyNetAlerts.Response.ChildAlertResponse;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import com.safetynet.SafetyNetAlerts.Service.FireStationService;
import com.safetynet.SafetyNetAlerts.Service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;


    @Mock
    private DataLoaderService dataLoaderService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private FireStationService fireStationService;


    private DataModel dataModel;
    private PersonModel person;
    private MedicalRecordModel medicalRecord;
    private FireStationModel fireStation;
    private PersonModel child, parent;
    private MedicalRecordModel childMedicalRecord, parentMedicalRecord;


    @BeforeEach
    void setUp() {

        fireStation = new FireStationModel();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        person = new PersonModel();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Test City");
        person.setZip("12345");
        person.setPhone("555-1234");
        person.setEmail("john.doe@example.com");

        MedicalRecordModel medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setFirstName("Boyd");
        medicalRecord.setBirthdate(LocalDate.parse("1984-06-03"));
        medicalRecord.setMedications(List.of("aznol:350mg",
                "hydrapermazol:100mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));
    }

    @Test
    void testGetAllFireStations() {

        when(fireStationRepository.findAll()).thenReturn(List.of(fireStation));

        List<FireStationModel> result = fireStationService.getAllFireStations();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(fireStationRepository, times(1)).findAll();
    }

    @Test
    void testAddFireStation() {
        when(fireStationRepository.save(any(FireStationModel.class))).thenReturn(fireStation);

        FireStationModel result = fireStationService.addFireStation(fireStation);

        assertNotNull(result);
        assertEquals("123 Main St", result.getAddress());
        verify(fireStationRepository, times(1)).save(fireStation);
    }

    @Test
    void testUpdateFireStation() {
        when(fireStationRepository.findByAddress("123 Main St")).thenReturn(Optional.of(fireStation));
        when(fireStationRepository.save(any(FireStationModel.class))).thenReturn(fireStation);

        FireStationModel updated = new FireStationModel();
        updated.setStation("2");

        FireStationModel result = fireStationService.updateFireStation("123 Main St", updated);

        assertNotNull(result);
        assertEquals("2", result.getStation());
        verify(fireStationRepository, times(1)).save(any(FireStationModel.class));
    }

    @Test
    void testDeleteFireStation() {
        doNothing().when(fireStationRepository).deleteByAddress("123 Main St");
        fireStationService.deleteFireStation("123 Main St");
        verify(fireStationRepository, times(1)).deleteByAddress("123 Main St");
    }


    @Test
    void testGetFloodStations() {


        person = new PersonModel();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Test City");
        person.setZip("12345");
        person.setPhone("555-1234");

        fireStation = new FireStationModel();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate(LocalDate.parse("1990-01-01"));
        medicalRecord.setMedications(List.of("Aspirin"));
        medicalRecord.setAllergies(List.of("Peanuts"));

        dataModel = new DataModel();
        dataModel.setPersons(List.of(person));
        dataModel.setFireStation(List.of(fireStation));
        dataModel.setMedicalRecord(List.of(medicalRecord));

        when(dataLoaderService.getDataModel()).thenReturn(dataModel);
        when(personService.calculateAge(LocalDate.parse("1990-01-01"))).thenReturn(34);

        List<FloodStationResponse> result = fireStationService.getFloodStations(List.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
        assertEquals(1, result.get(0).getPersons().size());

        FloodStationResponse.ResidentInfo resident = result.get(0).getPersons().get(0);
        assertEquals("John", resident.getFirstName());
        assertEquals("Doe", resident.getLastName());
        assertEquals(34, resident.getAge());
        assertEquals("555-1234", resident.getPhone());
        assertTrue(resident.getMedications().contains("Aspirin"));
        assertTrue(resident.getAllergies().contains("Peanuts"));

        verify(dataLoaderService, times(1)).getDataModel();
        verify(personService, times(1)).calculateAge(LocalDate.parse("1990-01-01"));
    }

    @Test
    void testGetResidentsByAddress() {

        // Création de la personne
        person = new PersonModel();
        person.setFirstName("Alice");
        person.setLastName("Brown");
        person.setAddress("456 Elm St");
        person.setPhone("555-6789");

        // Caserne de pompiers associée à l'adresse
        fireStation = new FireStationModel();
        fireStation.setAddress("456 Elm St");
        fireStation.setStation("2");

        // Dossier médical
        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("Alice");
        medicalRecord.setLastName("Brown");
        medicalRecord.setBirthdate(LocalDate.parse("1985-05-10"));
        medicalRecord.setMedications(List.of("Ibuprofen"));
        medicalRecord.setAllergies(List.of("Pollen"));

        // Mock du modèle de données
        dataModel = new DataModel();
        dataModel.setPersons(List.of(person));
        dataModel.setFireStation(List.of(fireStation));
        dataModel.setMedicalRecord(List.of(medicalRecord));

        // Simule le chargement des données
        when(dataLoaderService.getDataModel()).thenReturn(dataModel);
        when(personService.calculateAge(LocalDate.parse("1985-05-10"))).thenReturn(39);

        FireResponse response = fireStationService.getResidentsByAddress("456 Elm St");

        assertNotNull(response);
        assertEquals("456 Elm St", response.getAddress());
        assertEquals(2, response.getStationNumber());
        assertEquals(1, response.getResidents().size());

        FloodStationResponse.ResidentInfo resident = response.getResidents().get(0);
        assertEquals("Alice", resident.getFirstName());
        assertEquals("Brown", resident.getLastName());
        assertEquals(39, resident.getAge());
        assertEquals("555-6789", resident.getPhone());
        assertTrue(resident.getMedications().contains("Ibuprofen"));
        assertTrue(resident.getAllergies().contains("Pollen"));

        verify(dataLoaderService, times(1)).getDataModel();
        verify(personService, times(1)).calculateAge(LocalDate.parse("1985-05-10"));
    }


    @Test
    void testGetChildAlert_WithChildren() {
        // Création de l'enfant (moins de 18 ans)
        child = new PersonModel();
        child.setFirstName("Tom");
        child.setLastName("Smith");
        child.setAddress("123 Maple St");
        child.setPhone("555-1234");

        // Création du parent
        parent = new PersonModel();
        parent.setFirstName("John");
        parent.setLastName("Smith");
        parent.setAddress("123 Maple St");
        parent.setPhone("555-5678");

        // Dossier médical de l'enfant (né en 2015 → 9 ans)
        childMedicalRecord = new MedicalRecordModel();
        childMedicalRecord.setFirstName("Tom");
        childMedicalRecord.setLastName("Smith");
        childMedicalRecord.setBirthdate(LocalDate.parse("2015-06-20"));

        // Dossier médical du parent (adulte)
        parentMedicalRecord = new MedicalRecordModel();
        parentMedicalRecord.setFirstName("John");
        parentMedicalRecord.setLastName("Smith");
        parentMedicalRecord.setBirthdate(LocalDate.parse("1980-08-10"));

        // Mock du modèle de données
        dataModel = new DataModel();
        dataModel.setPersons(List.of(child, parent));
        dataModel.setMedicalRecord(List.of(childMedicalRecord, parentMedicalRecord));

        // Simule le chargement des données
        when(dataLoaderService.getDataModel()).thenReturn(dataModel);

        // Simule le calcul de l'âge
        when(personService.calculateAge(LocalDate.parse("2015-06-20"))).thenReturn(9);
        when(personService.calculateAge(LocalDate.parse("1980-08-10"))).thenReturn(43);
        List<ChildAlertResponse> response = fireStationService.getChildAlert("123 Maple St");

        assertNotNull(response);
        assertEquals(1, response.size());

        ChildAlertResponse childInfo = response.get(0);
        assertEquals("Tom", childInfo.getFirstName());
        assertEquals("Smith", childInfo.getLastName());
        assertEquals(9, childInfo.getAge());
        assertEquals(1, childInfo.getFamilyMembers().size());

        ChildAlertResponse.FamilyMemberInfo familyMember = childInfo.getFamilyMembers().get(0);
        assertEquals("John", familyMember.getFirstName());
        assertEquals("Smith", familyMember.getLastName());

        verify(dataLoaderService, times(1)).getDataModel();
        verify(personService, times(1)).calculateAge(LocalDate.parse("2015-06-20"));
    }

    @Test
    void testGetChildAlert_NoChildren() {
        // Création de l'enfant (moins de 18 ans)
        child = new PersonModel();
        child.setFirstName("Tom");
        child.setLastName("Smith");
        child.setAddress("123 Maple St");
        child.setPhone("555-1234");

        // Création du parent
        parent = new PersonModel();
        parent.setFirstName("John");
        parent.setLastName("Smith");
        parent.setAddress("123 Maple St");
        parent.setPhone("555-5678");

        // Dossier médical de l'enfant (né en 2015 → 9 ans)
        childMedicalRecord = new MedicalRecordModel();
        childMedicalRecord.setFirstName("Tom");
        childMedicalRecord.setLastName("Smith");
        childMedicalRecord.setBirthdate(LocalDate.parse("2015-06-20"));

        // Dossier médical du parent (adulte)
        parentMedicalRecord = new MedicalRecordModel();
        parentMedicalRecord.setFirstName("John");
        parentMedicalRecord.setLastName("Smith");
        parentMedicalRecord.setBirthdate(LocalDate.parse("1980-08-10"));

        // Mock du modèle de données
        dataModel = new DataModel();
        dataModel.setPersons(List.of(child, parent));
        dataModel.setMedicalRecord(List.of(childMedicalRecord, parentMedicalRecord));

        // Simule le chargement des données
        when(dataLoaderService.getDataModel()).thenReturn(dataModel);

        // Simule le calcul de l'âge
        when(personService.calculateAge(LocalDate.parse("2015-06-20"))).thenReturn(9);
        when(personService.calculateAge(LocalDate.parse("1980-08-10"))).thenReturn(43);
        // Modifier l'âge de l'enfant pour être un adulte
        when(personService.calculateAge(LocalDate.parse("2015-06-20"))).thenReturn(19);

        List<ChildAlertResponse> response = fireStationService.getChildAlert("123 Maple St");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(dataLoaderService, times(1)).getDataModel();
        verify(personService, times(1)).calculateAge(LocalDate.parse("2015-06-20"));
    }

    @Test
    void testGetPersonsByStationNumber() {

        // Création d'un enfant (moins de 18 ans)
        child = new PersonModel();
        child.setFirstName("Alice");
        child.setLastName("Brown");
        child.setAddress("456 Oak St");
        child.setPhone("555-1111");

        // Création d'un adulte
        parent = new PersonModel();
        parent.setFirstName("Bob");
        parent.setLastName("Brown");
        parent.setAddress("456 Oak St");
        parent.setPhone("555-2222");

        // Dossier médical de l'enfant (né en 2015 → 9 ans)
        childMedicalRecord = new MedicalRecordModel();
        childMedicalRecord.setFirstName("Alice");
        childMedicalRecord.setLastName("Brown");
        childMedicalRecord.setBirthdate(LocalDate.parse("2015-06-20"));

        // Dossier médical de l'adulte (né en 1980 → 43 ans)
        parentMedicalRecord = new MedicalRecordModel();
        parentMedicalRecord.setFirstName("Bob");
        parentMedicalRecord.setLastName("Brown");
        parentMedicalRecord.setBirthdate(LocalDate.parse("1980-08-10"));

        // Caserne de pompiers associée
        fireStation = new FireStationModel();
        fireStation.setAddress("456 Oak St");
        fireStation.setStation("1");

        // Mock du modèle de données
        dataModel = new DataModel();
        dataModel.setPersons(List.of(child, parent));
        dataModel.setMedicalRecord(List.of(childMedicalRecord, parentMedicalRecord));
        dataModel.setFireStation(List.of(fireStation));

        // Simule le chargement des données
        when(dataLoaderService.getDataModel()).thenReturn(dataModel);

        // Simule le calcul de l'âge
        when(personService.calculateAge(LocalDate.parse("2015-06-20"))).thenReturn(9);
        when(personService.calculateAge(LocalDate.parse("1980-08-10"))).thenReturn(43);

        FireStationResponse response = fireStationService.getPersonsByStationNumber(1);

        assertNotNull(response);
        assertEquals(2, response.getPersons().size());
        assertEquals(1, response.getChildCount());
        assertEquals(1, response.getAdultCount());

        FireStationResponse.FireStationInfo childInfo = response.getPersons().get(0);
        assertEquals("Alice", childInfo.getFirstName());
        assertEquals("Brown", childInfo.getLastName());
        assertEquals("456 Oak St", childInfo.getAddress());
        assertEquals("555-1111", childInfo.getPhone());

        FireStationResponse.FireStationInfo adultInfo = response.getPersons().get(1);
        assertEquals("Bob", adultInfo.getFirstName());
        assertEquals("Brown", adultInfo.getLastName());
        assertEquals("456 Oak St", adultInfo.getAddress());
        assertEquals("555-2222", adultInfo.getPhone());

        verify(dataLoaderService, times(1)).getDataModel();
        verify(personService, times(1)).calculateAge(LocalDate.parse("2015-06-20"));
        verify(personService, times(1)).calculateAge(LocalDate.parse("1980-08-10"));
    }



}

