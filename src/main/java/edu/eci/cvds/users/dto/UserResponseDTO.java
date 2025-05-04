package edu.eci.cvds.users.dto;


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
public class UserResponseDTO extends BaseUserDTO {
    /**
     * Constructor to facilitate creation in tests and services.
     */
    public UserResponseDTO(
            String id,
            String idType,
            String fullName,
            Integer phone,
            String email,
            String role
    ) {
        setId(id);
        setIdType(idType);
        setFullName(fullName);
        setPhone(phone);
        setEmail(email);
        setRole(role);
    }
}
