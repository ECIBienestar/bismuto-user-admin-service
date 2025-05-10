package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.ErrorResponse;
import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.StudentResponseDTO;
import edu.eci.cvds.users.model.enums.Program;
import edu.eci.cvds.users.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for student-specific operations.
 * Handles student creation, queries, and updates.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Operations for student management")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
        summary = "Get student by ID", 
        description = "Retrieves detailed student information including emergency contacts."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Student found successfully",
            content = @Content(schema = @Schema(implementation = StudentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Student not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF', 'MEDICAL_STAFF') || authentication.principal.username == #id")
    public ResponseEntity<StudentResponseDTO> getStudentById(
            @Parameter(description = "Student ID (document number)", required = true)
            @PathVariable String id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @Operation(
        summary = "Create student", 
        description = "Registers a new student with their personal and academic information."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Student created successfully",
            content = @Content(schema = @Schema(implementation = StudentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid student data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Conflict - User with same ID or email already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO created = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Update student", 
        description = "Updates student information including academic details."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Student updated successfully",
            content = @Content(schema = @Schema(implementation = StudentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Student not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid student data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @Parameter(description = "Student ID to be updated", required = true)
            @PathVariable String id,
            @Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO updated = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Get students by program", 
        description = "Retrieves all students in a specific academic program."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Students retrieved successfully",
            content = @Content(schema = @Schema(implementation = StudentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/by-program/{program}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByProgram(
            @Parameter(description = "Academic program", required = true)
            @PathVariable Program program) {
        List<StudentResponseDTO> students = studentService.getStudentsByProgram(program);
        return ResponseEntity.ok(students);
    }

    @Operation(
        summary = "Get students by semester", 
        description = "Retrieves all students in a specific semester."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Students retrieved successfully",
            content = @Content(schema = @Schema(implementation = StudentResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/by-semester/{semester}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsBySemester(
            @Parameter(description = "Academic semester (1-12)", required = true)
            @PathVariable int semester) {
        List<StudentResponseDTO> students = studentService.getStudentsBySemester(semester);
        return ResponseEntity.ok(students);
    }

    @Operation(
        summary = "Delete student", 
        description = "Removes a student from the system. This operation is irreversible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Student deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Student not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID to be deleted", required = true)
            @PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
