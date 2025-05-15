package edu.eci.cvds.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDTO sampleUser;
    private UserRequestDTO sampleRequest;

    @BeforeEach
    void setUp() {
        sampleUser = UserResponseDTO.builder()
                .id("123")
                .fullName("Test User")
                .email("test@example.com")
                .role(Role.PREFECT)
                .active(true)
                .build();

        sampleRequest = UserRequestDTO.builder()
                .id("123")
                .fullName("Test User")
                .email("test@example.com")
                .role(Role.PREFECT)
                .build();
    }

    @Test
    void getAllUsersReturnsListOfUsers() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(sampleUser));

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        assertEquals("123", response.getBody().get(0).getId());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdReturnsUser() {
        when(userService.getUserById("123")).thenReturn(sampleUser);

        ResponseEntity<UserResponseDTO> response = userController.getUserById("123");

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(userService, times(1)).getUserById("123");
    }

    @Test
    void updateUserReturnsUpdatedUser() {
        when(userService.updateUser("123", sampleRequest)).thenReturn(sampleUser);

        ResponseEntity<UserResponseDTO> response = userController.updateUser("123", sampleRequest);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(userService, times(1)).updateUser("123", sampleRequest);
    }

    @Test
    void deleteUserReturnsNoContent() {
        doNothing().when(userService).deleteUserById("123");

        ResponseEntity<Void> response = userController.deleteUser("123");

        assertEquals("204 NO_CONTENT", String.valueOf(response.getStatusCode()));
        verify(userService, times(1)).deleteUserById("123");
    }

    @Test
    void getUsersByRoleReturnsFilteredUsers() {
        when(userService.getUsersByRole(Role.PREFECT)).thenReturn(Arrays.asList(sampleUser));

        ResponseEntity<List<UserResponseDTO>> response = userController.getUsersByRole(Role.PREFECT);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        assertEquals(Role.PREFECT, response.getBody().get(0).getRole());
        verify(userService, times(1)).getUsersByRole(Role.PREFECT);
    }

    @Test
    void getUserByEmailReturnsUser() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(sampleUser);

        ResponseEntity<UserResponseDTO> response = userController.getUserByEmail("test@example.com");

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    void searchUsersByEmailReturnsMatchingUsers() {
        when(userService.searchUsersByEmail("test")).thenReturn(Arrays.asList(sampleUser));

        ResponseEntity<List<UserResponseDTO>> response = userController.searchUsersByEmail("test");

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getEmail().contains("test"));
        verify(userService, times(1)).searchUsersByEmail("test");
    }
}