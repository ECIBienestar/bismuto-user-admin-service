package edu.eci.cvds.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Entity for tracking user activity within the system.
* Records actions performed by users for audit and tracking purposes.
* 
* @author Jesús Pinzón (Team Bismuto)
* @version 1.1
* @since 2025-05-09
*/
@Entity
@Table(name = "user_activity_logs")
@Data
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserActivityLog {

   @Id
   @Column(name = "log_id", length = 36)
   private String logId; // UUID string

   @ManyToOne(optional = false, fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   @NotNull(message = "User cannot be null")
   private User user;

   @Column(name = "timestamp", nullable = false)
   @NotNull(message = "Timestamp cannot be null")
   private LocalDateTime timestamp;

   @Column(name = "action", nullable = false, length = 100)
   @NotBlank(message = "Action cannot be blank")
   @Size(max = 100, message = "Action must be at most 100 characters")
   private String action;

   @Column(name = "description", length = 500)
   @Size(max = 500, message = "Description must be at most 500 characters")
   private String description;

   /**
    * Creates a new activity log entry for a user action.
    * Automatically generates UUID and sets current timestamp.
    *
    * @param user The user performing the action
    * @param action The action being performed
    * @param description Optional details about the action
    */
   public UserActivityLog(User user, String action, String description) {
       this.logId = UUID.randomUUID().toString();
       this.user = user;
       this.timestamp = LocalDateTime.now();
       this.action = action;
       this.description = description;
   }

   /**
    * Factory method to create an activity log with minimal information.
    * 
    * @param user The user performing the action
    * @param action The action being performed
    * @return A new UserActivityLog instance
    */
   public static UserActivityLog create(User user, String action) {
       return new UserActivityLog(user, action, null);
   }

   /**
    * Lifecycle callback to ensure ID and timestamp are set before persisting.
    */
   @PrePersist
   public void prePersist() {
       if (this.logId == null) {
           this.logId = UUID.randomUUID().toString();
       }
       if (this.timestamp == null) {
           this.timestamp = LocalDateTime.now();
       }
   }
}
