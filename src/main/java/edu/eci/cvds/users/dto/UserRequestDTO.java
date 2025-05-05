package edu.eci.cvds.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Payload DTO for creating or updating any User (including staff/admin roles).
 * Inherits all validation annotations from BaseUserDTO.
 */
@Getter
@Setter
public class UserRequestDTO extends BaseUserDTO {
    @Schema(description = "Role of the user (e.g., ADMIN, STAFF)", example = "ADMIN")
    @NotBlank
    private String role;
}
