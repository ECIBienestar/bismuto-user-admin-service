package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Relationship;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactRequestDTO {
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;
    
    @NotNull(message = "Phone number cannot be null")
    @Digits(integer = 15, fraction = 0, message = "Phone must be a valid number")
    private Long phone;
    
    @NotNull(message = "ID type cannot be null")
    private IdType idType;
    
    @NotBlank(message = "ID number cannot be blank")
    @Size(max = 20, message = "ID number must be at most 20 characters")
    private String idNumber;
    
    @NotNull(message = "Relationship cannot be null")
    private Relationship relationship;
}
