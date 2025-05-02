package edu.eci.cvds.users.dto;

import jakarta.validation.constraints.*;

/**
 * Payload DTO for creating or updating any User (including staff/admin roles).
 * Inherits all validation annotations from BaseUserDTO.
 */
public class UserRequestDTO extends BaseUserDTO {
    // No additional fields here
}
