package edu.eci.cvds.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload DTO for user authentication.
 * Contains the necessary fields for login credentials.
 * 
 * This DTO is used to transfer the username/email and password required for user authentication.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredentialsDTO {
    
    @Schema(description = "User ID or email", example = "user@example.com")
    @NotBlank(message = "Username/email cannot be blank")
    private String username;
    
    @Schema(description = "User password", example = "SecureP@ss123")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
