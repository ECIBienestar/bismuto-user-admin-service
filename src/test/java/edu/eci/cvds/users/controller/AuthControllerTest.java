package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.AuthResponseDTO;
import edu.eci.cvds.users.dto.CredentialsDTO;
import edu.eci.cvds.users.dto.PasswordUpdateDTO;
import edu.eci.cvds.users.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private CredentialsDTO credentials;
    private PasswordUpdateDTO passwordUpdate;
    private AuthResponseDTO authResponse;

    @BeforeEach
    void setup() {
        credentials = new CredentialsDTO("test@example.com", "password123");
        passwordUpdate = new PasswordUpdateDTO("oldPassword", "newPassword");
        authResponse = AuthResponseDTO.builder()
                .authenticated(true)
                .message("Success")
                .build();
    }

    @Test
    void testValidateCredentialsSuccess() {
        when(authService.validateCredentials(credentials)).thenReturn(authResponse);

        ResponseEntity<AuthResponseDTO> response = authController.validateCredentials(credentials);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isAuthenticated());
        verify(authService, times(1)).validateCredentials(credentials);
    }

    @Test
    void testUpdatePasswordSuccess() {
        String userId = "user123";
        when(authService.updatePassword(userId, passwordUpdate)).thenReturn(authResponse);

        ResponseEntity<AuthResponseDTO> response = authController.updatePassword(userId, passwordUpdate);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).updatePassword(userId, passwordUpdate);
    }

    @Test
    void testValidateCredentialsFailure() {
        AuthResponseDTO errorResponse = AuthResponseDTO.builder()
                .authenticated(false)
                .message("Invalid credentials")
                .build();
        when(authService.validateCredentials(credentials)).thenReturn(errorResponse);

        ResponseEntity<AuthResponseDTO> response = authController.validateCredentials(credentials);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isAuthenticated());
    }

    @Test
    void testUpdatePasswordFailure() {
        String userId = "user123";
        AuthResponseDTO errorResponse = AuthResponseDTO.builder()
                .authenticated(false)
                .message("Wrong password")
                .build();
        when(authService.updatePassword(userId, passwordUpdate)).thenReturn(errorResponse);

        ResponseEntity<AuthResponseDTO> response = authController.updatePassword(userId, passwordUpdate);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isAuthenticated());
    }
}