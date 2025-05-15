package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.ScheduleEntryDTO;
import edu.eci.cvds.users.dto.StaffResponseDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.model.enums.Specialty;
import edu.eci.cvds.users.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffControllerTest {

    @Mock
    private StaffService staffService;

    @InjectMocks
    private StaffController staffController;

    private StaffResponseDTO staffResponse;
    private UserRequestDTO userRequest;
    private ScheduleEntryDTO scheduleEntry;

    @BeforeEach
    void setup() {
        staffResponse = StaffResponseDTO.builder()
                .id("123")
                .fullName("Test Staff")
                .email("staff@test.com")
                .specialty(Specialty.GENERAL_MEDICINE)
                .build();

        userRequest = UserRequestDTO.builder()
                .id("123")
                .fullName("Test Staff")
                .email("staff@test.com")
                .build();

        scheduleEntry = ScheduleEntryDTO.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .build();
    }

    @Test
    void testGetStaffById() {
        when(staffService.getStaffById("123")).thenReturn(staffResponse);

        ResponseEntity<StaffResponseDTO> response = staffController.getStaffById("123");

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(staffService, times(1)).getStaffById("123");
    }

    @Test
    void testCreateStaff() {
        when(staffService.createStaff(userRequest)).thenReturn(staffResponse);

        ResponseEntity<StaffResponseDTO> response = staffController.createStaff(userRequest);

        assertEquals("201 CREATED", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(staffService, times(1)).createStaff(userRequest);
    }

    @Test
    void testAddScheduleEntry() {
        when(staffService.addStaffScheduleEntry("123", scheduleEntry)).thenReturn(scheduleEntry);

        ResponseEntity<ScheduleEntryDTO> response = staffController.addScheduleEntry("123", scheduleEntry);

        assertEquals("201 CREATED", String.valueOf(response.getStatusCode()));
        assertNotNull(response.getBody().getStartTime());
        verify(staffService, times(1)).addStaffScheduleEntry("123", scheduleEntry);
    }

    @Test
    void testRemoveScheduleEntry() {
        doNothing().when(staffService).removeStaffScheduleEntry("123", 1L);

        ResponseEntity<Void> response = staffController.removeScheduleEntry("123", 1L);

        assertEquals("204 NO_CONTENT", String.valueOf(response.getStatusCode()));
        verify(staffService, times(1)).removeStaffScheduleEntry("123", 1L);
    }

    @Test
    void testGetAvailableStaff() {
        LocalDate date = LocalDate.now();
        when(staffService.getAvailableStaff(date)).thenReturn(Arrays.asList(staffResponse));

        ResponseEntity<List<StaffResponseDTO>> response = staffController.getAvailableStaff(date);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        verify(staffService, times(1)).getAvailableStaff(date);
    }

    @Test
    void testGetStaffBySpecialty() {
        when(staffService.getStaffBySpecialty("medicine")).thenReturn(Arrays.asList(staffResponse));

        ResponseEntity<List<StaffResponseDTO>> response = staffController.getStaffBySpecialty("medicine");

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        verify(staffService, times(1)).getStaffBySpecialty("medicine");
    }

    @Test
    void testDeleteStaff() {
        doNothing().when(staffService).deleteStaff("123");

        ResponseEntity<Void> response = staffController.deleteStaff("123");

        assertEquals("204 NO_CONTENT", String.valueOf(response.getStatusCode()));
        verify(staffService, times(1)).deleteStaff("123");
    }
}