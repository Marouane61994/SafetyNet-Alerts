package com.safetynet.SafetyNetAlerts.TestUnitaire;

import com.safetynet.SafetyNetAlerts.Model.*;
import com.safetynet.SafetyNetAlerts.Repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.Repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.Response.ChildAlertResponse;
import com.safetynet.SafetyNetAlerts.Response.FireResponse;
import com.safetynet.SafetyNetAlerts.Response.FireStationResponse;
import com.safetynet.SafetyNetAlerts.Response.FloodStationResponse;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import com.safetynet.SafetyNetAlerts.Service.FireStationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

    @InjectMocks
    private FireStationService fireStationService;
    @Mock
    private  FireStationRepository fireStationRepository;
    @Mock
    private  DataLoaderService dataLoaderService;
    @Mock
    private  PersonRepository personRepository;


    @Test
    void testGetAllFireStations() {
        //faire les when
        //when(personRepository.findAll()).thenReturn(fireStations);

        List<FireStationModel> fireStations = fireStationService.getAllFireStations();
        assertNotNull(fireStations);
        assertEquals(3, fireStations.size());
    }

    @Test
    void testAddFireStation() {
        FireStationModel newFireStation = new FireStationModel();
        fireStationService.addFireStation(newFireStation);

        List<FireStationModel> fireStations = fireStationService.getAllFireStations();
        assertEquals(3, fireStations.size());
        assertTrue(fireStations.stream().anyMatch(fs -> fs.getAddress().equals("1509 Culver St")));
    }

    @Test
    void testUpdateFireStation() {
        FireStationModel updatedFireStation = new FireStationModel();
        FireStationModel result = fireStationService.updateFireStation("123 Main St", updatedFireStation);

        assertEquals("10", result.getStation());
        assertEquals("123 Main St", result.getAddress());
    }

    @Test
    void testDeleteFireStation() {
        fireStationService.deleteFireStation("123 Main St");
        List<FireStationModel> fireStations = fireStationService.getAllFireStations();
        assertEquals(1, fireStations.size());
        assertFalse(fireStations.stream().anyMatch(fs -> fs.getAddress().equals("123 Main St")));
    }


    @Test
    void testGetResidentsByAddress() {
        FireResponse response = fireStationService.getResidentsByAddress("123 Main St");
        assertNotNull(response);
        assertEquals(1, response.getStationNumber());
        assertEquals(2, response.getResidents().size());

        FloodStationResponse.ResidentInfo resident = response.getResidents().stream()
                .filter(r -> r.getFirstName().equals("John") && r.getLastName().equals("Doe"))
                .findFirst()
                .orElse(null);
        assertNotNull(resident);
        assertEquals(33, resident.getAge());
        assertEquals("123-456-7890", resident.getPhone());
    }

    @Test
    void testGetPhoneNumbersByFireStation() {
        List<String> phoneNumbers = fireStationService.getPhoneNumbersByFireStation(1);
        assertNotNull(phoneNumbers);
        assertEquals(2, phoneNumbers.size());
        assertTrue(phoneNumbers.contains("123-456-7890"));
    }

    @Test
    void testGetChildAlert() {
        List<ChildAlertResponse> children = fireStationService.getChildAlert("123 Main St");
        assertNotNull(children);
        assertEquals(1, children.size());

        ChildAlertResponse child = children.get(0);
        assertEquals("Jane", child.getFirstName());
        assertEquals(18, child.getAge());
        assertEquals(1, child.getFamilyMembers().size());
    }

    @Test
    void testGetPersonsByStationNumber() {
        FireStationResponse response = fireStationService.getPersonsByStationNumber(1);
        assertNotNull(response);
        assertEquals(2, response.getPersons().size());
        assertEquals(1, response.getChildCount());
        assertEquals(1, response.getAdultCount());
    }
}


