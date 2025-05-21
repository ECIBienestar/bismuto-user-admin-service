package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.AuthResponseDTO;
import edu.eci.cvds.users.dto.CredentialsDTO;
import edu.eci.cvds.users.dto.PasswordUpdateDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.Student;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Constructor; // Importación añadida
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private Student testStudent;
    private CredentialsDTO validCredentials;
    private CredentialsDTO invalidCredentials;

    @BeforeEach
    void setUp() throws Exception {  // Añadido throws Exception
        MockitoAnnotations.openMocks(this);

        // Solución usando reflection para acceder al constructor protegido
        Constructor<Student> constructor = Student.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        testStudent = constructor.newInstance();

        // Configurar propiedades
        testStudent.setId("123");
        testStudent.setFullName("Test User");
        testStudent.setEmail("test@test.com");
        testStudent.setPassword("encodedPassword");
        testStudent.setRole(Role.STUDENT);
        testStudent.setActive(true);
        testStudent.setStudentCode("STU123");
        testStudent.setSemester(5);
        testStudent.setBirthDate(LocalDate.now());
        testStudent.setAddress("123 Main St");

        validCredentials = new CredentialsDTO("123", "correctPassword");
        invalidCredentials = new CredentialsDTO("123", "wrongPassword");
    }

    @Test
    void validateCredentialsShouldAuthenticateWithValidIdAndPassword() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);

        AuthResponseDTO response = authService.validateCredentials(validCredentials);

        assertTrue(response.isAuthenticated());
        assertEquals("Authentication successful", response.getMessage());
        assertEquals("123", response.getId());
        verify(userRepository).findById("123");
    }

    @Test
    void validateCredentialsShouldAuthenticateWithValidEmailAndPassword() {
        CredentialsDTO emailCredentials = new CredentialsDTO("test@test.com", "correctPassword");
        when(userRepository.findById("test@test.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);

        AuthResponseDTO response = authService.validateCredentials(emailCredentials);

        assertTrue(response.isAuthenticated());
        assertEquals("test@test.com", response.getEmail());
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void validateCredentialsShouldFailWhenUserNotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("999")).thenReturn(Optional.empty());

        AuthResponseDTO response = authService.validateCredentials(new CredentialsDTO("999", "password"));

        assertFalse(response.isAuthenticated());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void validateCredentialsShouldFailWhenUserInactive() {
        testStudent.setActive(false);
        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));

        AuthResponseDTO response = authService.validateCredentials(validCredentials);

        assertFalse(response.isAuthenticated());
        assertEquals("User account is inactive", response.getMessage());
    }

    @Test
    void validateCredentialsShouldFailWithWrongPassword() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        AuthResponseDTO response = authService.validateCredentials(invalidCredentials);

        assertFalse(response.isAuthenticated());
        assertEquals("Invalid credentials", response.getMessage());
    }

    @Test
    void updatePasswordShouldSuccessWithValidCurrentPassword() {
        PasswordUpdateDTO passwordUpdate = new PasswordUpdateDTO("currentPassword", "newPassword");

        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("currentPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        AuthResponseDTO response = authService.updatePassword("123", passwordUpdate);

        assertTrue(response.isAuthenticated());
        assertEquals("Password updated successfully", response.getMessage());
        verify(userRepository).save(testStudent);
        assertEquals("newEncodedPassword", testStudent.getPassword());
    }

    @Test
    void updatePasswordShouldThrowWhenUserNotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authService.updatePassword("999", new PasswordUpdateDTO("old", "new"));
        });
    }

    @Test
    void updatePasswordShouldThrowWithInvalidCurrentPassword() {
        PasswordUpdateDTO passwordUpdate = new PasswordUpdateDTO("wrongPassword", "newPassword");

        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> {
            authService.updatePassword("123", passwordUpdate);
        });
        verify(userRepository, never()).save(any());
    }
}