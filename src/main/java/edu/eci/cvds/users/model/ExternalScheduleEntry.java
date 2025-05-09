package edu.eci.cvds.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
* Entity representing a time slot in a staff member's external schedule.
* Contains start and end times for availability periods.
* 
* @author Jesús Pinzón (Team Bismuto)
* @version 1.1
* @since 2025-05-09
*/
@Entity
@Table(name = "external_schedule_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalScheduleEntry {
   
   @Id 
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "start_time", nullable = false)
   @NotNull(message = "Start time cannot be null")
   private LocalDateTime startTime;

   @Column(name = "end_time", nullable = false)
   @NotNull(message = "End time cannot be null")
   @Future(message = "End time must be in the future")
   private LocalDateTime endTime;
   
   /**
    * Custom validation method to ensure end time is after start time.
    * 
    * @return true if the end time is after the start time
    */
   @AssertTrue(message = "End time must be after start time")
   @Transient
   public boolean isValidTimeRange() {
       if (startTime == null || endTime == null) {
           return true; // Let @NotNull handle this
       }
       return endTime.isAfter(startTime);
   }
   
   /**
    * Checks if this schedule entry overlaps with another entry.
    * 
    * @param other The other schedule entry to check against
    * @return true if there is an overlap, false otherwise
    */
   public boolean overlaps(ExternalScheduleEntry other) {
       return (startTime.isBefore(other.endTime) && 
               endTime.isAfter(other.startTime));
   }
}
