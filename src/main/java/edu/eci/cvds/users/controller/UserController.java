package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.ErrorResponse;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for basic user operations common to all user types.
 * Handles general user queries and updates.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Basic operations for all user types")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Get all users", 
        description = "Retrieves a list of all users in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Get user by ID", 
        description = "Retrieves a specific user by their unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User found successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') || authentication.principal.username == #id")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID (document number)", required = true)
            @PathVariable String id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Update user", 
        description = "Updates basic user information."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') || authentication.principal.username == #id")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID to be updated", required = true)
            @PathVariable String id,
            @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Delete user", 
        description = "Removes a user from the system. This operation is irreversible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID to be deleted", required = true)
            @PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get users by role", 
        description = "Retrieves all users with a specific role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @Parameter(description = "Role to filter by", required = true)
            @PathVariable Role role) {
        List<UserResponseDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}
