package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.StudentResponseDTO;
import edu.eci.cvds.users.model.enums.Program;
import edu.eci.cvds.users.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private StudentResponseDTO studentResponse;
    private StudentRequestDTO studentRequest;

    @BeforeEach
    void setup() {
        studentResponse = StudentResponseDTO.builder()
                .id("123")
                .fullName("Test Student")
                .email("student@test.com")
                .program(Program.SOFTWARE_ENGINEERING)
                .semester(5)
                .build();

        studentRequest = StudentRequestDTO.builder()
                .id("123")
                .fullName("Test Student")
                .email("student@test.com")
                .program(Program.SOFTWARE_ENGINEERING)
                .semester(5)
                .build();
    }

    @Test
    void testGetStudentById() {
        when(studentService.getStudentById("123")).thenReturn(studentResponse);

        ResponseEntity<StudentResponseDTO> response = studentController.getStudentById("123");

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(studentService, times(1)).getStudentById("123");
    }

    @Test
    void testCreateStudent() {
        when(studentService.createStudent(studentRequest)).thenReturn(studentResponse);

        ResponseEntity<StudentResponseDTO> response = studentController.createStudent(studentRequest);

        assertEquals("201 CREATED", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(studentService, times(1)).createStudent(studentRequest);
    }

    @Test
    void testUpdateStudent() {
        when(studentService.updateStudent("123", studentRequest)).thenReturn(studentResponse);

        ResponseEntity<StudentResponseDTO> response = studentController.updateStudent("123", studentRequest);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals("123", response.getBody().getId());
        verify(studentService, times(1)).updateStudent("123", studentRequest);
    }

    @Test
    void testGetStudentsByProgram() {
        when(studentService.getStudentsByProgram(Program.SOFTWARE_ENGINEERING))
                .thenReturn(Arrays.asList(studentResponse));

        ResponseEntity<List<StudentResponseDTO>> response = studentController
                .getStudentsByProgram(Program.SOFTWARE_ENGINEERING);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        verify(studentService, times(1)).getStudentsByProgram(Program.SOFTWARE_ENGINEERING);
    }

    @Test
    void testGetStudentsBySemester() {
        when(studentService.getStudentsBySemester(5))
                .thenReturn(Arrays.asList(studentResponse));

        ResponseEntity<List<StudentResponseDTO>> response = studentController.getStudentsBySemester(5);

        assertEquals("200 OK", String.valueOf(response.getStatusCode()));
        assertEquals(1, response.getBody().size());
        verify(studentService, times(1)).getStudentsBySemester(5);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentService).deleteStudent("123");

        ResponseEntity<Void> response = studentController.deleteStudent("123");

        assertEquals("204 NO_CONTENT", String.valueOf(response.getStatusCode()));
        verify(studentService, times(1)).deleteStudent("123");
    }
}