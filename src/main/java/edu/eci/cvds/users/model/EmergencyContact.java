package edu.eci.cvds.users.model;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Relationship;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Entity representing an emergency contact for a student.
* Contains contact information and relationship to the student.
* 
* @author Jesús Pinzón (Team Bismuto)
* @version 1.1
* @since 2025-05-09
*/
@Entity
@Table(name = "emergency_contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContact {
   
   @Id 
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "id_number", nullable = false, length = 20)
   @NotBlank(message = "ID number cannot be blank")
   @Size(max = 20, message = "ID number must be at most 20 characters")
   private String idNumber;

   @Enumerated(EnumType.STRING)
   @Column(name = "id_type", nullable = false, length = 20)
   private IdType idType;

   @Column(name = "full_name", nullable = false)
   @NotBlank(message = "Full name cannot be blank")
   @Size(max = 100, message = "Full name must be at most 100 characters")
   private String fullName;

   @Column(nullable = false)
   @NotNull(message = "Phone number cannot be null")
   @Digits(integer = 15, fraction = 0, message = "Phone must be a valid number")
   private Long phone;   

   @Column(nullable = false)
   private Relationship relationship;
   
   /**
    * Formats the contact information for notification purposes.
    * 
    * @return Formatted string with contact details
    */
   public String getFormattedContactInfo() {
       return String.format("%s (%s): %d", 
                            fullName, 
                            relationship.toString(), 
                            phone);
   }
}
