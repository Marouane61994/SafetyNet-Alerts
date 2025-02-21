package com.safetynet.SafetyNetAlerts.testUnitaire;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNetAlerts.model.DataModel;
import com.safetynet.SafetyNetAlerts.service.DataLoaderService;
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
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataLoaderService dataLoaderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReadJsonFromFile_Success() throws IOException {

        DataModel mockDataModel = new DataModel();
        mock(InputStream.class);

        when(objectMapper.readValue(any(InputStream.class), eq(DataModel.class))).thenReturn(mockDataModel);

        dataLoaderService.readJsonFromFile();

        assertNotNull(dataLoaderService.getDataModel(), "Le DataModel ne doit pas être null");
    }
}

