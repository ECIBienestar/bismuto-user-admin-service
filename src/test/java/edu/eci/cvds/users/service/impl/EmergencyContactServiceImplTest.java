package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.EmergencyContactDTO;
import edu.eci.cvds.users.dto.EmergencyContactRequestDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.EmergencyContact;
import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Relationship;
import edu.eci.cvds.users.repository.EmergencyContactRepository;
import edu.eci.cvds.users.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyContactServiceImplTest {

    @Mock
    private EmergencyContactRepository contactRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private EmergencyContactServiceImpl emergencyContactService;

    private EmergencyContact testContact;
    private EmergencyContactRequestDTO testRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testContact = EmergencyContact.builder()
                .id(1L)
                .fullName("John Doe")
                .phone("1234567890")
                .idType(IdType.CC)
                .idNumber("123456789")
                .relationship(Relationship.FATHER)
                .build();

        testRequestDTO = EmergencyContactRequestDTO.builder()
                .fullName("John Doe")
                .phone("1234567890")
                .idType(IdType.CC)
                .idNumber("123456789")
                .relationship(Relationship.FATHER)
                .build();
    }

    @Test
    void createEmergencyContactShouldReturnCreatedContact() {
        // Arrange
        when(contactRepository.save(any(EmergencyContact.class))).thenReturn(testContact);

        // Act
        EmergencyContactDTO result = emergencyContactService.createEmergencyContact(testRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("1234567890", result.getPhone());
        verify(contactRepository).save(any(EmergencyContact.class));
    }

    @Test
    void getEmergencyContactByIdShouldReturnContactWhenExists() {
        // Arrange
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));

        // Act
        EmergencyContactDTO result = emergencyContactService.getEmergencyContactById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getFullName());
        verify(contactRepository).findById(1L);
    }

    @Test
    void getEmergencyContactByIdShouldThrowWhenNotFound() {
        // Arrange
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            emergencyContactService.getEmergencyContactById(99L);
        });
        verify(contactRepository).findById(99L);
    }

    @Test
    void updateEmergencyContactShouldUpdateExistingContact() {
        // Arrange
        EmergencyContactRequestDTO updateDto = EmergencyContactRequestDTO.builder()
                .fullName("Updated Name")
                .phone("9876543210")
                .idType(IdType.NIP)
                .idNumber("987654321")
                .relationship(Relationship.GRANDPARENT)
                .build();

        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));
        when(contactRepository.save(any(EmergencyContact.class))).thenReturn(testContact);

        // Act
        EmergencyContactDTO result = emergencyContactService.updateEmergencyContact(1L, updateDto);

        // Assert
        assertEquals("Updated Name", result.getFullName());
        assertEquals("9876543210", result.getPhone());
        assertEquals(IdType.NIP, result.getIdType());
        verify(contactRepository).findById(1L);
        verify(contactRepository).save(any(EmergencyContact.class));
    }

    @Test
    void updateEmergencyContactShouldThrowWhenNotFound() {
        // Arrange
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            emergencyContactService.updateEmergencyContact(99L, testRequestDTO);
        });
        verify(contactRepository).findById(99L);
        verify(contactRepository, never()).save(any());
    }

    @Test
    void deleteEmergencyContactShouldDeleteWhenExistsAndNotReferenced() {
        // Arrange
        when(contactRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.existsByEmergencyContactId(1L)).thenReturn(false);
        doNothing().when(contactRepository).deleteById(1L);

        // Act
        emergencyContactService.deleteEmergencyContact(1L);

        // Assert
        verify(contactRepository).existsById(1L);
        verify(studentRepository).existsByEmergencyContactId(1L);
        verify(contactRepository).deleteById(1L);
    }

    @Test
    void deleteEmergencyContactShouldThrowWhenNotFound() {
        // Arrange
        when(contactRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            emergencyContactService.deleteEmergencyContact(99L);
        });
        verify(contactRepository).existsById(99L);
        verify(contactRepository, never()).deleteById(any());
    }

    @Test
    void deleteEmergencyContactShouldThrowWhenReferencedByStudent() {
        // Arrange
        when(contactRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.existsByEmergencyContactId(1L)).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emergencyContactService.deleteEmergencyContact(1L);
        });

        assertEquals("Cannot delete emergency contact that is referenced by students", exception.getMessage());
        verify(contactRepository).existsById(1L);
        verify(studentRepository).existsByEmergencyContactId(1L);
        verify(contactRepository, never()).deleteById(any());
    }

    @Test
    void getAllEmergencyContactsShouldReturnAllContacts() {
        // Arrange
        EmergencyContact contact2 = EmergencyContact.builder()
                .id(2L)
                .fullName("Jane Smith")
                .phone("0987654321")
                .idType(IdType.CC)
                .idNumber("987654321")
                .relationship(Relationship.MOTHER)
                .build();

        when(contactRepository.findAll()).thenReturn(List.of(testContact, contact2));

        // Act
        List<EmergencyContactDTO> result = emergencyContactService.getAllEmergencyContacts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("Jane Smith", result.get(1).getFullName());
        verify(contactRepository).findAll();
    }
}