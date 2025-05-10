package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base DTO with common fields for all user types.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseUserDTO {

    @NotBlank(message = "ID cannot be blank")
    @Size(max = 50, message = "ID must be at most 50 characters")
    private String id;

    private IdType idType;

    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Phone number cannot be blank")
    // @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone must be a valid number with 8-15 digits, optionally starting with '+'")
    private String phone;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 50, message = "Email must be at most 50 characters")
    private String email;
}
