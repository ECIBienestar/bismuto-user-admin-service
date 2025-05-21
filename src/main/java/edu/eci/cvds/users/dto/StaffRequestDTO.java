package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Payload DTO for creating or updating Staff members.
 * Extends UserRequestDTO with staff-specific fields like specialty.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StaffRequestDTO extends UserRequestDTO {
    
    @Schema(
        description = "Staff specialty or area of expertise", 
        example = "GENERAL_MEDICINE",
        nullable = true,
        allowableValues = {
            "GENERAL_MEDICINE", "DENTISTRY", "PSYCHOLOGY", "NUTRITION", 
            "PHYSIOTHERAPY", "NURSING", "FITNESS_COACH", "YOGA_INSTRUCTOR", 
            "PILATES_INSTRUCTOR", "CARDIO_COACH", "STRENGTH_COACH", 
            "SWIMMING_COACH", "SOCCER_COACH", "BASKETBALL_COACH", 
            "VOLLEYBALL_COACH", "DANCE_INSTRUCTOR", "WELLNESS_COORDINATOR", 
            "ADMINISTRATIVE", "OTHER"
        }
    )
    private Specialty specialty;
}
