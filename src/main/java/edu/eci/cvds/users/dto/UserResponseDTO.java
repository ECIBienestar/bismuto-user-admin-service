package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Response DTO returned by user endpoints.
 * Contains the base user information for API responses.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserResponseDTO {
    private String id;
    private IdType idType;
    private String fullName;
    private Long phone;
    private String email;
    private Role role;
    
    /**
     * Constructor to facilitate creation in services.
     */
    public UserResponseDTO(
            String id,
            IdType idType,
            String fullName,
            Long phone,
            String email,
            Role role
    ) {
        this.id = id;
        this.idType = idType;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }
}
