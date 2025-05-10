package edu.eci.cvds.users.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.cvds.users.dto.ErrorResponse;
import edu.eci.cvds.users.dto.UserActivityLogDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.service.UserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for administrative operations on users.
 * Provides endpoints for user management, auditing and reporting.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "User Administration", description = "Administrative endpoints for user management")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class UserAdminController {
    private final UserAdminService adminService;

    public UserAdminController(UserAdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(
        summary = "Change user status", 
        description = "Activates or deactivates a user account."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User status changed successfully",
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
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> changeUserStatus(
            @Parameter(description = "User ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Active status", required = true)
            @RequestParam boolean active) {
        UserResponseDTO updated = adminService.changeUserStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Get user activity logs", 
        description = "Retrieves audit logs for a specific user or all users."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Logs retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserActivityLogDTO.class))
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
    @GetMapping("/logs")
    public ResponseEntity<List<UserActivityLogDTO>> getUserActivityLogs(
            @Parameter(description = "User ID (optional)")
            @RequestParam(required = false) String userId,
            @Parameter(description = "Start date and time (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<UserActivityLogDTO> logs = adminService.getUserActivityLogs(userId, startDate, endDate);
        return ResponseEntity.ok(logs);
    }

    @Operation(
        summary = "Get user statistics", 
        description = "Retrieves statistical data about system users."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Statistics retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> statistics = adminService.getUserStatistics();
        return ResponseEntity.ok(statistics);
    }

    @Operation(
        summary = "Change user role", 
        description = "Updates the role assigned to a user."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User role changed successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid role",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> changeUserRole(
            @Parameter(description = "User ID", required = true)
            @PathVariable String id,
            @Parameter(description = "New role", required = true)
            @RequestParam String role) {
        UserResponseDTO updated = adminService.changeUserRole(id, role);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Delete user with options", 
        description = "Removes a user with additional options like soft delete or cascading."
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
            responseCode = "400", 
            description = "Invalid delete options",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}/with-options")
    public ResponseEntity<Void> deleteUserWithOptions(
            @Parameter(description = "User ID to be deleted", required = true)
            @PathVariable String id,
            @Parameter(description = "Soft delete (mark as inactive instead of removing)", required = false)
            @RequestParam(defaultValue = "false") boolean softDelete,
            @Parameter(description = "Delete associated data (like logs and activity history)", required = false)
            @RequestParam(defaultValue = "false") boolean deleteAssociatedData) {
        adminService.deleteUserWithOptions(id, softDelete, deleteAssociatedData);
        return ResponseEntity.noContent().build();
    }
}
