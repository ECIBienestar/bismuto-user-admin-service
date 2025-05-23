package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.AuthResponseDTO;
import edu.eci.cvds.users.dto.CredentialsDTO;
import edu.eci.cvds.users.dto.ErrorResponse;
import edu.eci.cvds.users.dto.PasswordResetDTO;
import edu.eci.cvds.users.dto.PasswordUpdateDTO;
import edu.eci.cvds.users.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "Validate credentials", 
        description = "Validates user credentials for authentication purposes."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Credentials validated"
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/validate")
    public ResponseEntity<AuthResponseDTO> validateCredentials(
            @Valid @RequestBody CredentialsDTO credentials) {
        AuthResponseDTO response = authService.validateCredentials(credentials);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update password with id", 
        description = "Updates a user's password"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Password updated successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Current password is incorrect",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/password/{userId}")
    public ResponseEntity<AuthResponseDTO> updatePassword(
            @PathVariable String userId,
            @Valid @RequestBody PasswordUpdateDTO passwordUpdate) {
        AuthResponseDTO response = authService.updatePassword(userId, passwordUpdate);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Reset password by email", 
        description = "Resets a user's password using their email address"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Password reset successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request data or user is inactive",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found with the provided email",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/reset-password")
    public ResponseEntity<AuthResponseDTO> resetPasswordByEmail(
            @Valid @RequestBody PasswordResetDTO passwordReset) {
        AuthResponseDTO response = authService.resetPasswordByEmail(passwordReset);
        return ResponseEntity.ok(response);
    }
}
