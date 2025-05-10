package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Program;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Payload DTO for creating or updating a Student.
 * Inherits all common fields from BaseUserDTO.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudentRequestDTO extends UserRequestDTO {
    
    @Schema(description = "Unique student code", example = "A00123456")
    @NotBlank(message = "Student code cannot be blank")
    @Size(max = 12, message = "Student code must be at most 12 characters")
    private String studentCode;

    @Schema(description = "Academic program", example = "SOFTWARE_ENGINEERING")
    @NotNull(message = "Program cannot be null")
    private Program program;
    
    @Schema(description = "Current semester", example = "5")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 12, message = "Semester must be at most 12")
    private int semester;

    @Schema(description = "Student's date of birth", example = "2000-05-20")
    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Schema(description = "Home address", example = "Cra 7 #45-67 Bogotá")
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 100, message = "Address must be at most 100 characters")
    private String address;

    @Schema(description = "ID of the emergency contact", example = "987654321")
    @NotNull(message = "Emergency contact ID cannot be null")
    private Long emergencyContactId;
}
