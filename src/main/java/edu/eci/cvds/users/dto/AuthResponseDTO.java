package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload DTO for authentication responses.
 * Contains the details of the authenticated user and the authentication status.
 * 
 * This DTO is used to transfer the result of an authentication attempt,
 * including user details and a success/failure message.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.0
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String id;
    private String fullName;
    private String email;
    private Role role;
    private boolean authenticated;
    private String message;
}
