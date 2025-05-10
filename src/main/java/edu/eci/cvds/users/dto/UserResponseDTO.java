package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Response DTO returned by user endpoints.
 * Contains the base user information for API responses.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponseDTO {
    private String id;
    private IdType idType;
    private String fullName;
    private String phone;
    private String email;
    private Role role;
    private boolean active;
}
