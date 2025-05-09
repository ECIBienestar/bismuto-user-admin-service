package edu.eci.cvds.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
* Entity representing staff members in the university wellness system.
* Extends the base User class with staff-specific attributes.
* 
* @author Jesús Pinzón (Team Bismuto)
* @version 1.1
* @since 2025-05-09
*/
@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SuperBuilder
public class Staff extends User {
   
   @Column(nullable = true)
   @Size(max = 50, message = "Specialty must be at most 50 characters")
   private String specialty; // only for MEDICAL_STAFF
   
   @Builder.Default
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
   @JoinColumn(name = "staff_id")
   private List<ExternalScheduleEntry> availableSchedule = new ArrayList<>();
   
   /**
    * Adds a schedule entry to the staff member's available schedule.
    * 
    * @param entry The schedule entry to add
    * @return true if successfully added
    */
   public boolean addScheduleEntry(ExternalScheduleEntry entry) {
       return this.availableSchedule.add(entry);
   }
   
   /**
    * Removes a schedule entry from the staff member's available schedule.
    * 
    * @param entry The schedule entry to remove
    * @return true if successfully removed
    */
   public boolean removeScheduleEntry(ExternalScheduleEntry entry) {
       return this.availableSchedule.remove(entry);
   }
}
