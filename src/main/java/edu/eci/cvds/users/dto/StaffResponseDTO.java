package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for Staff entities with additional staff-specific fields.
 * Extends UserResponseDTO with specialty and schedule information.
 *
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Staff information with specialty and schedule details")
public class StaffResponseDTO extends UserResponseDTO {
    
    /**
     * Specialty or area of expertise (mainly for medical staff)
     */
    @Schema(description = "Staff specialty or area of expertise", 
            example = "Cardiology", 
            nullable = true)
    private String specialty;
    
    /**
     * List of scheduled availability time slots
     */
    @Schema(description = "Available schedule slots")
    private List<ScheduleEntryDTO> availableSchedule = new ArrayList<>();
    
    /**
     * Full constructor with all fields
     */
    public StaffResponseDTO(
            String id, 
            IdType idType, 
            String fullName, 
            Long phone, 
            String email, 
            Role role, 
            String specialty, 
            List<ScheduleEntryDTO> availableSchedule) {
        super(id, idType, fullName, phone, email, role);
        this.specialty = specialty;
        this.availableSchedule = availableSchedule != null ? 
                                 availableSchedule : 
                                 new ArrayList<>();
    }
    
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
