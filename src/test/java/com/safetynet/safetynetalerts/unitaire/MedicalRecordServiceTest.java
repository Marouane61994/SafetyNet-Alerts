package com.safetynet.safetynetalerts.unitaire;

import com.safetynet.safetynetalerts.model.MedicalRecordModel;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private MedicalRecordModel medicalRecord;


    @Test
    void testGetAllMedicalRecords() {

        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate(LocalDate.of(1984, 3, 6));
        medicalRecord.setMedications(Arrays.asList("aznol", "hydrapermazol"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        when(medicalRecordRepository.findAll()).thenReturn(List.of(medicalRecord));

        List<MedicalRecordModel> records = medicalRecordService.getAllMedicalRecords();

        assertThat(records).hasSize(1);
        assertThat(records.get(0).getFirstName()).isEqualTo("John");
        verify(medicalRecordRepository, times(1)).findAll();
    }

    @Test
    void testAddMedicalRecord() {

        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate(LocalDate.of(1984, 3, 6));
        medicalRecord.setMedications(Arrays.asList("aznol", "hydrapermazol"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        when(medicalRecordRepository.save(medicalRecord)).thenReturn(medicalRecord);

        MedicalRecordModel savedRecord = medicalRecordService.addMedicalRecord(medicalRecord);

        assertThat(savedRecord).isNotNull();
        assertThat(savedRecord.getFirstName()).isEqualTo("John");
        verify(medicalRecordRepository, times(1)).save(medicalRecord);
    }

    @Test
    void testUpdateMedicalRecord_Success() {

        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate(LocalDate.of(1984, 3, 6));
        medicalRecord.setMedications(Arrays.asList("aznol", "hydrapermazol"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        MedicalRecordModel updatedRecord = new MedicalRecordModel();
        updatedRecord.setBirthdate(LocalDate.of(1984, 3, 6));
        updatedRecord.setMedications(List.of("Paracetamol"));
        updatedRecord.setAllergies(List.of("Pollen"));

        when(medicalRecordRepository.findByFullName("John", "Boyd")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordRepository.save(any(MedicalRecordModel.class))).thenReturn(updatedRecord);

        MedicalRecordModel result = medicalRecordService.updateMedicalRecord("John", "Boyd", updatedRecord);

        assertThat(result.getBirthdate()).isEqualTo(LocalDate.of(1984, 3, 6));
        assertThat(result.getMedications()).containsExactly("Paracetamol");
        assertThat(result.getAllergies()).containsExactly("Pollen");
        verify(medicalRecordRepository, times(1)).save(medicalRecord);
    }

    @Test
    void testUpdateMedicalRecord_NotFound() {

        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate(LocalDate.of(1984, 3, 6));
        medicalRecord.setMedications(Arrays.asList("aznol", "hydrapermazol"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        when(medicalRecordRepository.findByFullName("John", "Boyd")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> medicalRecordService.updateMedicalRecord("John", "Boyd", medicalRecord));

        verify(medicalRecordRepository, never()).save(any(MedicalRecordModel.class));
    }

    @Test
    void testDeleteMedicalRecord() {

        medicalRecord = new MedicalRecordModel();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate(LocalDate.of(1984, 3, 6));
        medicalRecord.setMedications(Arrays.asList("aznol", "hydrapermazol"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        doNothing().when(medicalRecordRepository).deleteByFullName("John", "Boyd");

        medicalRecordService.deleteMedicalRecord("John", "Boyd");

        verify(medicalRecordRepository, times(1)).deleteByFullName("John", "Boyd");
    }
}
