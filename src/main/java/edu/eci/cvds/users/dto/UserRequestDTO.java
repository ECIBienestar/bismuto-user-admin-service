package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Payload DTO for creating or updating any User (including staff/admin roles).
 * Inherits all validation annotations from BaseUserDTO.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserRequestDTO extends BaseUserDTO {
    
    @Schema(description = "Role of the user", example = "ADMINISTRATOR")
    private Role role;
    
    @Schema(description = "Initial password for the user", example = "SecureP@ss123")
    private String password;
}
