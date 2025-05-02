package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.User;
import jakarta.validation.constraints.*;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Payload DTO for creating or updating a Student.
 * Hereda de UserRequestDTO (y por tanto de BaseUserDTO) todos los campos comunes.
 */
@Getter @Setter
public class StudentRequestDTO extends UserRequestDTO {
    @NotBlank
    private String studentCode;

    @NotBlank
    private String program;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    private String address;

    @NotBlank
    private String emergencyContactId;
}
