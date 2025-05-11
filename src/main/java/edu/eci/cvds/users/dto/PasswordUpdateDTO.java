package edu.eci.cvds.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload DTO for updating a user's password.
 * Contains validation annotations to ensure proper input.
 * 
 * This DTO is used when a user needs to update their password securely.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordUpdateDTO {
    
    @Schema(description = "Current password", example = "OldPassword123")
    @NotBlank(message = "Current password cannot be blank")
    private String currentPassword;
    
    @Schema(description = "New password", example = "NewSecureP@ss456")
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
