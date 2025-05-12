package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.EmergencyContactDTO;
import edu.eci.cvds.users.dto.EmergencyContactRequestDTO;
import edu.eci.cvds.users.service.EmergencyContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyContactControllerTest {

    @Mock
    private EmergencyContactService contactService;

    @InjectMocks
    private EmergencyContactController emergencyContactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    void createEmergencyContactShouldReturnCreated() {
        EmergencyContactRequestDTO requestDTO = new EmergencyContactRequestDTO();
        EmergencyContactDTO expectedDTO = new EmergencyContactDTO();
        when(contactService.createEmergencyContact(requestDTO)).thenReturn(expectedDTO);

        ResponseEntity<EmergencyContactDTO> response = emergencyContactController.createEmergencyContact(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(contactService).createEmergencyContact(requestDTO);
    }

    @Test
    @WithMockUser(authorities = {"WELLNESS_STAFF"})
    void getEmergencyContactByIdShouldReturnContact() {
        Long id = 1L;
        EmergencyContactDTO expectedDTO = new EmergencyContactDTO();
        when(contactService.getEmergencyContactById(id)).thenReturn(expectedDTO);

        ResponseEntity<EmergencyContactDTO> response = emergencyContactController.getEmergencyContactById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(contactService).getEmergencyContactById(id);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    void updateEmergencyContactShouldReturnUpdatedContact() {
        Long id = 1L;
        EmergencyContactRequestDTO requestDTO = new EmergencyContactRequestDTO();
        EmergencyContactDTO expectedDTO = new EmergencyContactDTO();
        when(contactService.updateEmergencyContact(id, requestDTO)).thenReturn(expectedDTO);

        ResponseEntity<EmergencyContactDTO> response = emergencyContactController.updateEmergencyContact(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(contactService).updateEmergencyContact(id, requestDTO);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    void deleteEmergencyContactShouldReturnNoContent() {
        Long id = 1L;

        ResponseEntity<Void> response = emergencyContactController.deleteEmergencyContact(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(contactService).deleteEmergencyContact(id);
    }

    @Test
    @WithMockUser(authorities = {"WELLNESS_STAFF"})
    void getAllEmergencyContactsShouldReturnList() {
        List<EmergencyContactDTO> expectedList = Arrays.asList(new EmergencyContactDTO(), new EmergencyContactDTO());
        when(contactService.getAllEmergencyContacts()).thenReturn(expectedList);

        ResponseEntity<List<EmergencyContactDTO>> response = emergencyContactController.getAllEmergencyContacts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(contactService).getAllEmergencyContacts();
    }

    @Test
    @WithMockUser(authorities = {"MEDICAL_STAFF"})
    void getEmergencyContactByIdWithMedicalStaffShouldReturnContact() {
        Long id = 1L;
        EmergencyContactDTO expectedDTO = new EmergencyContactDTO();
        when(contactService.getEmergencyContactById(id)).thenReturn(expectedDTO);

        ResponseEntity<EmergencyContactDTO> response = emergencyContactController.getEmergencyContactById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(contactService).getEmergencyContactById(id);
    }
}