package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.EmergencyContactDTO;
import edu.eci.cvds.users.dto.EmergencyContactRequestDTO;
import edu.eci.cvds.users.dto.ErrorResponse;
import edu.eci.cvds.users.service.EmergencyContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing emergency contacts.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@RestController
@RequestMapping("/api/emergency-contacts")
@Tag(name = "Emergency Contacts", description = "Endpoints for managing emergency contacts")
@SecurityRequirement(name = "bearerAuth")
public class EmergencyContactController {
    private final EmergencyContactService contactService;

    public EmergencyContactController(EmergencyContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(
        summary = "Create emergency contact", 
        description = "Creates a new emergency contact that can be assigned to students."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Emergency contact created successfully",
            content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid emergency contact data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<EmergencyContactDTO> createEmergencyContact(
            @Valid @RequestBody EmergencyContactRequestDTO dto) {
        EmergencyContactDTO created = contactService.createEmergencyContact(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Get emergency contact by ID", 
        description = "Retrieves an emergency contact by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Emergency contact found successfully",
            content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Emergency contact not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF', 'MEDICAL_STAFF')")
    public ResponseEntity<EmergencyContactDTO> getEmergencyContactById(
            @Parameter(description = "Emergency contact ID", required = true)
            @PathVariable Long id) {
        EmergencyContactDTO contact = contactService.getEmergencyContactById(id);
        return ResponseEntity.ok(contact);
    }

    @Operation(
        summary = "Update emergency contact", 
        description = "Updates information for an existing emergency contact."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Emergency contact updated successfully",
            content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Emergency contact not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid emergency contact data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<EmergencyContactDTO> updateEmergencyContact(
            @Parameter(description = "Emergency contact ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EmergencyContactRequestDTO dto) {
        EmergencyContactDTO updated = contactService.updateEmergencyContact(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Delete emergency contact", 
        description = "Removes an emergency contact from the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Emergency contact deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Emergency contact not found",
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
    public ResponseEntity<Void> deleteEmergencyContact(
            @Parameter(description = "Emergency contact ID", required = true)
            @PathVariable Long id) {
        contactService.deleteEmergencyContact(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all emergency contacts", 
        description = "Retrieves all emergency contacts in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Emergency contacts retrieved successfully",
            content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'WELLNESS_STAFF')")
    public ResponseEntity<List<EmergencyContactDTO>> getAllEmergencyContacts() {
        List<EmergencyContactDTO> contacts = contactService.getAllEmergencyContacts();
        return ResponseEntity.ok(contacts);
    }
}
