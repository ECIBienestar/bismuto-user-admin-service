package edu.eci.cvds.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Payload DTO for creating or updating a Student.
 * Hereda de UserRequestDTO todos los campos comunes.
 */
@Getter
@Setter
public class StudentRequestDTO extends BaseUserDTO {
    @Schema(description = "Unique student code", example = "A00123456")
    @NotBlank
    private String studentCode;

    @Schema(description = "Academic program", example = "Computer Science")
    @NotBlank
    private String program;

    @Schema(description = "Student's date of birth", example = "2000-05-20")
    @NotNull
    private LocalDate birthDate;

    @Schema(description = "Home address", example = "Cra 7 #45-67 Bogotá")
    @NotBlank
    private String address;

    @Schema(description = "ID of the emergency contact", example = "987654321")
    @NotNull(message = "Emergency contact ID cannot be null")
    private Long emergencyContactId;
}
