package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Relationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for emergency contact information.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContactDTO {
    private Long id;
    private String fullName;
    private String phone;
    private IdType idType;
    private String idNumber;
    private Relationship relationship;
}
