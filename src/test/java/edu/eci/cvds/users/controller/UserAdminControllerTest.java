package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.UserActivityLogDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.service.UserAdminService;
import edu.eci.cvds.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAdminControllerTest {

    @Mock
    private UserAdminService adminService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserAdminController userAdminController;

    private UserResponseDTO sampleUser;
    private UserActivityLogDTO sampleLog;

    @BeforeEach
    void setUp() {
        sampleUser = new UserResponseDTO();
        sampleUser.setId("123");
        sampleUser.setFullName("Test User");
        sampleUser.setActive(true);

        sampleLog = new UserActivityLogDTO();
        sampleLog.setUserId("123");
        sampleLog.setAction("LOGIN");
        sampleLog.setTimestamp(LocalDateTime.now());
    }

    @Test
    void changeUserStatusShouldReturnUpdatedUser() {
        when(adminService.changeUserStatus("123", false)).thenReturn(sampleUser);

        ResponseEntity<UserResponseDTO> response = userAdminController.changeUserStatus("123", false);

        assertEquals("200 OK", String.valueOf(String.valueOf(response.getStatusCode())));
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getId());
        verify(adminService, times(1)).changeUserStatus("123", false);
    }

    @Test
    void getUserActivityLogsShouldReturnLogs() {
        List<UserActivityLogDTO> logs = Arrays.asList(sampleLog);
        when(adminService.getUserActivityLogs("123", null, null)).thenReturn(logs);

        ResponseEntity<List<UserActivityLogDTO>> response = userAdminController.getUserActivityLogs("123", null, null);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, Integer.valueOf(response.getBody().size()));
        assertEquals("123", response.getBody().get(0).getUserId());
    }

    @Test
    void getUserStatisticsShouldReturnStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 10);
        stats.put("activeUsers", 7);
        when(adminService.getUserStatistics()).thenReturn(stats);

        ResponseEntity<Map<String, Object>> response = userAdminController.getUserStatistics();

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(10, response.getBody().get("totalUsers"));
        assertEquals(7, response.getBody().get("activeUsers"));
    }

    @Test
    void deleteUserWithOptionsShouldReturnNoContent() {
        doNothing().when(adminService).deleteUserWithOptions("123", true, false);

        ResponseEntity<Void> response = userAdminController.deleteUserWithOptions("123", true, false);

        assertEquals("204 NO_CONTENT", String.valueOf(response.getStatusCode()));
        assertNull(response.getBody());
        verify(adminService).deleteUserWithOptions("123", true, false);
    }

    @Test
    void deleteAllUsersShouldReturnSuccessMessage() {
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "All users have been deleted from the system");
        expectedResponse.put("deletedCount", 5L);
        expectedResponse.put("timestamp", LocalDateTime.now());

        when(userService.deleteAllUsers()).thenReturn(5L);

        ResponseEntity<Map<String, Object>> response = userAdminController.deleteAllUsers();

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("All users have been deleted from the system", response.getBody().get("message"));
        assertEquals(5L, response.getBody().get("deletedCount"));
    }

    @Test
    void getUserActivityLogsWithDateRange_ShouldReturnFilteredLogs() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<UserActivityLogDTO> logs = Arrays.asList(sampleLog);

        when(adminService.getUserActivityLogs(null, start, end)).thenReturn(logs);

        ResponseEntity<List<UserActivityLogDTO>> response = userAdminController.getUserActivityLogs(null, start, end);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deleteUserWithOptionsShouldCallServiceWithCorrectParams() {
        doNothing().when(adminService).deleteUserWithOptions("123", false, true);

        userAdminController.deleteUserWithOptions("123", false, true);

        verify(adminService).deleteUserWithOptions("123", false, true);
    }

    @Test
    void changeUserRoleShouldReturnUpdatedUser() {
        String userId = "123";
        String newRole = "MODERATOR";

        UserResponseDTO updatedUser = new UserResponseDTO();
        updatedUser.setId(userId);
        updatedUser.setFullName("Test User");
        updatedUser.setActive(true);

        when(adminService.changeUserRole(userId, newRole)).thenReturn(updatedUser);

        ResponseEntity<UserResponseDTO> response = userAdminController.changeUserRole(userId, newRole);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        verify(adminService).changeUserRole(userId, newRole);
    }

}