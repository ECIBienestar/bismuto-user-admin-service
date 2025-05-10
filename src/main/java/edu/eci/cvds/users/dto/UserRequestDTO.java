package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Payload DTO for creating or updating any User (including staff/admin roles).
 * Inherits all validation annotations from BaseUserDTO.
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
public class UserRequestDTO extends BaseUserDTO {
    
    @Schema(description = "Role of the user", example = "ADMINISTRATOR")
    private Role role;
}
