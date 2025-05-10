package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Relationship;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating emergency contacts.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContactRequestDTO {
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;
    
    @Column(nullable = false, length = 20)
    @NotBlank(message = "Phone number cannot be blank")
    // @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone must be a valid number with 8-15 digits, optionally starting with '+'")
    private String phone;
    
    private IdType idType;
    
    @NotBlank(message = "ID number cannot be blank")
    @Size(max = 20, message = "ID number must be at most 20 characters")
    private String idNumber;
    
    private Relationship relationship;
}
