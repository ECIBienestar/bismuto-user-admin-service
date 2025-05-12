package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.Student;
import edu.eci.cvds.users.model.Staff;
import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Program;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.model.enums.Specialty;
import edu.eci.cvds.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Student testStudent;
    private Staff testStaff;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testStudent = Student.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("Test Student")
                .phone("1234567890")
                .email("student@test.com")
                .role(Role.STUDENT)
                .password("password123")
                .active(true)
                .studentCode("STU123")
                .program(Program.BIOTECHNOLOGY_ENGINEERING)
                .semester(5)
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("123 Main St")
                .build();

        testStaff = Staff.builder()
                .id("456")
                .idType(IdType.CC)
                .fullName("Test Staff")
                .phone("0987654321")
                .email("staff@test.com")
                .role(Role.ADMINISTRATOR)
                .password("password123")
                .active(true)
                .specialty(Specialty.PSYCHOLOGY)
                .build();
    }

    @Test
    void getAllUsersShouldReturnListOfUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(testStudent, testStaff));

        // Act
        List<UserResponseDTO> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getId());
        assertEquals("456", result.get(1).getId());
        verify(userRepository).findAll();
    }

    @Test
    void getUserByIdShouldReturnUserWhenExists() {
        // Arrange
        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));

        // Act
        UserResponseDTO result = userService.getUserById("123");

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("Test Student", result.getFullName());
        verify(userRepository).findById("123");
    }

    @Test
    void getUserByIdShouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById("999");
        });
        verify(userRepository).findById("999");
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        // Arrange
        UserRequestDTO updateDto = UserRequestDTO.builder()
                .fullName("Updated Name")
                .idType(IdType.CC)
                .phone("9876543210")
                .email("updated@test.com")
                .build();

        when(userRepository.findById("123")).thenReturn(Optional.of(testStudent));
        when(userRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        UserResponseDTO result = userService.updateUser("123", updateDto);

        // Assert
        assertEquals("123", result.getId());
        assertEquals("Updated Name", result.getFullName());
        assertEquals(IdType.CC, result.getIdType());
        assertEquals("9876543210", result.getPhone());
        assertEquals("updated@test.com", result.getEmail());

        verify(userRepository).findById("123");
        verify(userRepository).save(testStudent);
    }

    @Test
    void deleteUserByIdShouldDeleteWhenUserExists() {
        // Arrange
        when(userRepository.existsById("123")).thenReturn(true);

        // Act
        userService.deleteUserById("123");

        // Assert
        verify(userRepository).existsById("123");
        verify(userRepository).deleteById("123");
    }

    @Test
    void deleteUserByIdShouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.existsById("999")).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUserById("999");
        });
        verify(userRepository).existsById("999");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteAllUsersShouldReturnCountOfDeletedUsers() {
        // Arrange
        when(userRepository.count()).thenReturn(5L);
        doNothing().when(userRepository).deleteAll();

        // Act
        long deletedCount = userService.deleteAllUsers();

        // Assert
        assertEquals(5L, deletedCount);
        verify(userRepository).count();
        verify(userRepository).deleteAll();
    }

    @Test
    void getUsersByRoleShouldReturnFilteredList() {
        // Arrange
        when(userRepository.findByRole(Role.ADMINISTRATOR)).thenReturn(Arrays.asList(testStaff));

        // Act
        List<UserResponseDTO> result = userService.getUsersByRole(Role.ADMINISTRATOR);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Role.ADMINISTRATOR, result.get(0).getRole());
        verify(userRepository).findByRole(Role.ADMINISTRATOR);
    }

    @Test
    void getUserByEmailShouldReturnUserWhenExists() {
        // Arrange
        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(testStudent));

        // Act
        UserResponseDTO result = userService.getUserByEmail("student@test.com");

        // Assert
        assertNotNull(result);
        assertEquals("student@test.com", result.getEmail());
        verify(userRepository).findByEmail("student@test.com");
    }

    @Test
    void getUserByEmailShouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@test.com");
        });
        verify(userRepository).findByEmail("nonexistent@test.com");
    }

    @Test
    void searchUsersByEmailShouldReturnMatchingUsers() {
        // Arrange
        when(userRepository.findByEmailContainingIgnoreCase("test")).thenReturn(Arrays.asList(testStudent, testStaff));

        // Act
        List<UserResponseDTO> result = userService.searchUsersByEmail("test");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getEmail().contains("test"));
        assertTrue(result.get(1).getEmail().contains("test"));
        verify(userRepository).findByEmailContainingIgnoreCase("test");
    }
}