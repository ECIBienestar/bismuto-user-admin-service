package edu.eci.cvds.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO returned by user endpoints.
 * Extiende BaseUserDTO para exponer los mismos campos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO extends BaseUserDTO {
}
