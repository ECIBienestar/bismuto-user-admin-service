package edu.eci.cvds.users.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Payload DTO for creating or updating any User (including staff/admin roles).
 * Inherits all validation annotations from BaseUserDTO.
 */
@Getter
@Setter
public class UserRequestDTO extends BaseUserDTO {
}
