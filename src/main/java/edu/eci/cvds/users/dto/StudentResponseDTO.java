package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Program;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Response DTO for Student entities with additional student-specific fields.
 * Extends UserResponseDTO with academic and personal information.
 *
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Student information with academic and personal details")
public class StudentResponseDTO extends UserResponseDTO {
    
    /**
     * Unique student code assigned by the university
     */
    @Schema(description = "Unique student code", example = "A00123456")
    private String studentCode;
    
    /**
     * Academic program in which the student is enrolled
     */
    @Schema(description = "Academic program", example = "SOFTWARE_ENGINEERING")
    private Program program;
    
    /**
     * Current semester of the student (1-12)
     */
    @Schema(description = "Current semester (1-12)", example = "5")
    private int semester;
    
    /**
     * Student's date of birth
     */
    @Schema(description = "Date of birth", example = "2000-05-20")
    private LocalDate birthDate;
    
    /**
     * Student's residential address
     */
    @Schema(description = "Residential address", example = "Cra 7 #45-67 Bogotá")
    private String address;
    
    /**
     * Emergency contact information
     */
    @Schema(description = "Emergency contact information")
    private EmergencyContactDTO emergencyContact;
}
