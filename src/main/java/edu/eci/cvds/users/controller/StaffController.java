package edu.eci.cvds.users.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.cvds.users.dto.ErrorResponse;
import edu.eci.cvds.users.dto.ScheduleEntryDTO;
import edu.eci.cvds.users.dto.StaffResponseDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.model.enums.Specialty;
import edu.eci.cvds.users.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for staff-specific operations.
 * Handles staff creation, queries, updates and schedule management.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff", description = "Operations for staff management")
@SecurityRequirement(name = "bearerAuth")
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(
        summary = "Get staff by ID", 
        description = "Retrieves detailed staff information including specialties and schedules."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Staff found successfully",
            content = @Content(schema = @Schema(implementation = StaffResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Staff not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF') || authentication.principal.username == #id")
    public ResponseEntity<StaffResponseDTO> getStaffById(
            @Parameter(description = "Staff ID (document number)", required = true)
            @PathVariable String id) {
        StaffResponseDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @Operation(
        summary = "Create staff member", 
        description = "Registers a new staff user (administrator, medical staff, trainer, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Staff created successfully",
            content = @Content(schema = @Schema(implementation = StaffResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid staff data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Conflict - User with same ID or email already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<StaffResponseDTO> createStaff(
            @Valid @RequestBody UserRequestDTO dto) {
        StaffResponseDTO created = staffService.createStaff(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Add schedule entry", 
        description = "Adds a new availability time slot for a staff member."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Schedule entry added successfully",
            content = @Content(schema = @Schema(implementation = ScheduleEntryDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Staff not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid schedule data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/{id}/schedule")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') || authentication.principal.username == #id")
    public ResponseEntity<ScheduleEntryDTO> addScheduleEntry(
            @Parameter(description = "Staff ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody ScheduleEntryDTO dto) {
        ScheduleEntryDTO created = staffService.addStaffScheduleEntry(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Remove schedule entry", 
        description = "Removes an availability time slot from a staff member's schedule."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Schedule entry removed successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Schedule entry not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{staffId}/schedule/{entryId}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') || authentication.principal.username == #staffId")
    public ResponseEntity<Void> removeScheduleEntry(
            @Parameter(description = "Staff ID", required = true)
            @PathVariable String staffId,
            @Parameter(description = "Schedule entry ID", required = true)
            @PathVariable Long entryId) {
        staffService.removeStaffScheduleEntry(staffId, entryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get available staff by date", 
        description = "Retrieves all staff members available on a specific date."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Staff retrieved successfully",
            content = @Content(schema = @Schema(implementation = StaffResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid date format",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/available")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<StaffResponseDTO>> getAvailableStaff(
            @Parameter(description = "Date to check availability (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate date) {
        List<StaffResponseDTO> availableStaff = staffService.getAvailableStaff(date);
        return ResponseEntity.ok(availableStaff);
    }

    @Operation(
        summary = "Get staff by specialty", 
        description = "Retrieves all staff members with a specific specialty."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Staff retrieved successfully",
            content = @Content(schema = @Schema(implementation = StaffResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/by-specialty")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<StaffResponseDTO>> getStaffBySpecialty(
            @Parameter(description = "Specialty to filter by", required = true)
            @RequestParam String specialty) {
        List<StaffResponseDTO> staff = staffService.getStaffBySpecialty(specialty);
        return ResponseEntity.ok(staff);
    }

    @Operation(
    summary = "Delete staff member", 
    description = "Removes a staff member from the system. This operation is irreversible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Staff member deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Staff member not found",
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
    public ResponseEntity<Void> deleteStaff(
            @Parameter(description = "Staff ID to be deleted", required = true)
            @PathVariable String id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
