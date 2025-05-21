package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.StudentResponseDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.DuplicateResourceException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.EmergencyContact;
import edu.eci.cvds.users.model.Student;
import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Program;
import edu.eci.cvds.users.repository.EmergencyContactRepository;
import edu.eci.cvds.users.repository.StudentRepository;
import edu.eci.cvds.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmergencyContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentRequestDTO testRequestDTO;
    private Student testStudent;
    private EmergencyContact testContact;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testContact = EmergencyContact.builder()
                .id(1L)
                .fullName("Parent Doe")
                .phone("1234567890")
                .build();

        testRequestDTO = StudentRequestDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("John Doe")
                .phone("1234567890")
                .email("john@test.com")
                .studentCode("STU123")
                .program(Program.MECHANICAL_ENGINEERING)
                .semester(5)
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("123 Main St")
                .emergencyContactId(1L)
                .build();

        testStudent = Student.builder()
                .id("123")
                .fullName("John Doe")
                .email("john@test.com")
                .studentCode("STU123")
                .emergencyContact(testContact)
                .build();
    }

    @Test
    void createStudentShouldReturnCreatedStudent() {
        // Arrange
        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentCode("STU123")).thenReturn(Optional.empty());
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        StudentResponseDTO result = studentService.createStudent(testRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("John Doe", result.getFullName());
        verify(userRepository).existsById("123");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudentShouldThrowWhenIdExists() {
        // Arrange
        when(userRepository.existsById("123")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            studentService.createStudent(testRequestDTO);
        });
        verify(userRepository).existsById("123");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void createStudentShouldThrowWhenEmailExists() {
        // Arrange
        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testStudent));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            studentService.createStudent(testRequestDTO);
        });
        verify(userRepository).findByEmail("john@test.com");
    }

    @Test
    void createStudentShouldThrowWhenStudentCodeExists() {
        // Arrange
        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentCode("STU123")).thenReturn(Optional.of(testStudent));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            studentService.createStudent(testRequestDTO);
        });
        verify(studentRepository).findByStudentCode("STU123");
    }

    @Test
    void createStudentShouldGenerateDefaultPassword() {
        // Arrange
        testRequestDTO.setPassword(null);
        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.empty());
        when(studentRepository.findByStudentCode("STU123")).thenReturn(Optional.empty());
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));
        when(passwordEncoder.encode("STU123@2000")).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        studentService.createStudent(testRequestDTO);

        // Assert
        verify(passwordEncoder).encode("STU123@2000");
    }

    @Test
    void getStudentByIdShouldReturnStudentWhenExists() {
        // Arrange
        when(studentRepository.findById("123")).thenReturn(Optional.of(testStudent));

        // Act
        StudentResponseDTO result = studentService.getStudentById("123");

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        verify(studentRepository).findById("123");
    }

    @Test
    void updateStudentShouldUpdateFields() {
        // Arrange
        StudentRequestDTO updateDto = StudentRequestDTO.builder()
                .fullName("Updated Name")
                .email("updated@test.com")
                .studentCode("STU456")
                .emergencyContactId(1L)
                .build();

        when(studentRepository.findById("123")).thenReturn(Optional.of(testStudent));
        when(studentRepository.findByStudentCode("STU456")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("updated@test.com")).thenReturn(Optional.empty());
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        StudentResponseDTO result = studentService.updateStudent("123", updateDto);

        // Assert
        assertEquals("Updated Name", result.getFullName());
        verify(studentRepository).save(testStudent);
    }

    @Test
    void deleteStudentShouldDeleteWhenExists() {
        // Arrange
        when(studentRepository.existsById("123")).thenReturn(true);

        // Act
        studentService.deleteStudent("123");

        // Assert
        verify(studentRepository).deleteById("123");
    }

    @Test
    void getStudentsByProgramShouldReturnFilteredList() {
        // Arrange - Crear estudiante con todos los campos necesarios
        Student testStudentWithProgram = Student.builder()
                .id("123")
                .fullName("John Doe")
                .email("john@test.com")
                .studentCode("STU123")
                .program(Program.MECHANICAL_ENGINEERING)
                .semester(5)
                .emergencyContact(testContact)
                .build();

        // Configurar mock para devolver el estudiante con programa
        when(studentRepository.findByProgram(Program.MECHANICAL_ENGINEERING))
                .thenReturn(List.of(testStudentWithProgram));

        // Act
        List<StudentResponseDTO> result = studentService.getStudentsByProgram(Program.MECHANICAL_ENGINEERING);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Program.MECHANICAL_ENGINEERING, result.get(0).getProgram());
        verify(studentRepository).findByProgram(Program.MECHANICAL_ENGINEERING);
    }

    @Test
    void getStudentsBySemesterShouldReturnFilteredList() {
        // Arrange - Crear estudiante con semestre definido
        Student testStudentWithSemester = Student.builder()
                .id("123")
                .fullName("John Doe")
                .email("john@test.com")
                .studentCode("STU123")
                .program(Program.MECHANICAL_ENGINEERING)
                .semester(5) // Semestre definido explícitamente
                .emergencyContact(testContact)
                .build();

        // Configurar mock para devolver el estudiante con semestre
        when(studentRepository.findBySemester(5))
                .thenReturn(List.of(testStudentWithSemester));

        // Act
        List<StudentResponseDTO> result = studentService.getStudentsBySemester(5);

        // Assert
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getSemester());
        verify(studentRepository).findBySemester(5);
    }

    @Test
    void getStudentsBySemesterShouldThrowForInvalidSemester() {
        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            studentService.getStudentsBySemester(0);
        });

        assertThrows(BadRequestException.class, () -> {
            studentService.getStudentsBySemester(13);
        });
    }

    @Test
    void updateStudentShouldThrowWhenStudentCodeExistsForAnotherStudent() {
        // Arrange
        String studentId = "123";
        String existingStudentCode = "STU456";

        Student currentStudent = Student.builder()
                .id(studentId)
                .studentCode("STU123")
                .build();

        Student anotherStudent = Student.builder()
                .id("999") // Diferente ID
                .studentCode(existingStudentCode)
                .build();

        StudentRequestDTO updateDto = StudentRequestDTO.builder()
                .studentCode(existingStudentCode)
                .email("new@email.com")
                .emergencyContactId(1L)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(currentStudent));
        when(studentRepository.findByStudentCode(existingStudentCode)).thenReturn(Optional.of(anotherStudent));
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            studentService.updateStudent(studentId, updateDto);
        });

        verify(studentRepository).findByStudentCode(existingStudentCode);
    }

    @Test
    void updateStudentShouldThrowWhenEmailExistsForAnotherUser() {
        // Arrange
        String studentId = "123";
        String existingEmail = "existing@email.com";

        Student currentStudent = Student.builder()
                .id(studentId)
                .email("old@email.com")
                .build();

        Student anotherUser = Student.builder()
                .id("999") // Diferente ID
                .email(existingEmail)
                .build();

        StudentRequestDTO updateDto = StudentRequestDTO.builder()
                .studentCode("STU123")
                .email(existingEmail)
                .emergencyContactId(1L)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(currentStudent));
        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(anotherUser));
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            studentService.updateStudent(studentId, updateDto);
        });

        verify(userRepository).findByEmail(existingEmail);
    }

    @Test
    void deleteStudentShouldThrowWhenStudentNotFound() {
        // Arrange
        String nonExistentId = "999";
        when(studentRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.deleteStudent(nonExistentId);
        });

        // Verify
        verify(studentRepository).existsById(nonExistentId);
        verify(studentRepository, never()).deleteById(anyString());
    }

    @Test
    void createStudentShouldUseProvidedPasswordWhenNotNull() {
        // Arrange
        String providedPassword = "MySecurePassword123";
        StudentRequestDTO dto = StudentRequestDTO.builder()
                .id("123")
                .email("test@test.com")
                .studentCode("STU123")
                .emergencyContactId(1L)
                .password(providedPassword) // Contraseña proporcionada
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        when(userRepository.existsById(any())).thenReturn(false);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(studentRepository.findByStudentCode(any())).thenReturn(Optional.empty());
        when(contactRepository.findById(any())).thenReturn(Optional.of(testContact));
        when(passwordEncoder.encode(providedPassword)).thenReturn("encodedPassword");
        when(studentRepository.save(any())).thenReturn(testStudent);

        // Act
        studentService.createStudent(dto);

        // Assert
        verify(passwordEncoder).encode(providedPassword); // Verifica que se usó la contraseña proporcionada
    }
}