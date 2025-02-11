package com.safetynet.SafetyNetAlerts.TestUnitaire;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNetAlerts.Model.DataModel;
import com.safetynet.SafetyNetAlerts.Service.DataLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataLoaderServiceTest {

    @Mock
    private ObjectMapper objectMapper; // Mock ObjectMapper

    @InjectMocks
    private DataLoaderService dataLoaderService; // Injecter le mock

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks
    }

    @Test
    void testReadJsonFromFile_Success() throws IOException {
        // Simuler un fichier JSON avec un DataModel factice
        DataModel mockDataModel = new DataModel();
        mock(InputStream.class);

        // Simuler la lecture de JSON avec ObjectMapper
        when(objectMapper.readValue(any(InputStream.class), eq(DataModel.class))).thenReturn(mockDataModel);

        // Exécuter la méthode
        dataLoaderService.readJsonFromFile();

        // Vérifier que l'objet DataModel est bien chargé
        assertNotNull(dataLoaderService.getDataModel(), "Le DataModel ne doit pas être null");
    }




}

