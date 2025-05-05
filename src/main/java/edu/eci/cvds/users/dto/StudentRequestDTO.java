package edu.eci.cvds.users.dto;

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
    @NotBlank
    private String studentCode;

    @NotBlank
    private String program;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    private String address;
  
    @NotNull(message = "Emergency contact ID cannot be null")
    private Long emergencyContactId;
}
