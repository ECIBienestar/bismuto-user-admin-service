package edu.eci.cvds.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.eci.cvds.users.model.enums.Specialty;

/**
 * Response DTO for Staff entities with additional staff-specific fields.
 * Extends UserResponseDTO with specialty and schedule information.
 *
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Staff information with specialty and schedule details")
public class StaffResponseDTO extends UserResponseDTO {
    
    /**
     * Specialty or area of expertise (mainly for medical staff)
     */
    @Schema(description = "Staff specialty or area of expertise", 
            example = "DENTISTRY", 
            nullable = true)
    private Specialty specialty;
    
    /**
     * List of scheduled availability time slots
     */
    @Schema(description = "Available schedule slots")
    @lombok.Builder.Default
    private List<ScheduleEntryDTO> availableSchedule = new ArrayList<>();
    
    /**
     * Adds a schedule entry to the staff's available schedule.
     * Helper method for controller responses.
     *
     * @param entry The schedule entry to add
     */
    public void addScheduleEntry(ScheduleEntryDTO entry) {
        if (this.availableSchedule == null) {
            this.availableSchedule = new ArrayList<>();
        }
        this.availableSchedule.add(entry);
    }
}
