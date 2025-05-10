package edu.eci.cvds.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user activity log information returned to API clients.
 * Represents audit records of user actions in the system.
 *
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityLogDTO {
    
    /**
     * Unique identifier for the log entry
     */
    private String logId;
    
    /**
     * ID of the user who performed the action
     */
    private String userId;
    
    /**
     * Full name of the user who performed the action
     */
    private String userFullName;
    
    /**
     * Date and time when the action was performed
     */
    private LocalDateTime timestamp;
    
    /**
     * Type of action performed (e.g., "LOGIN", "UPDATE_PROFILE", "CREATE_USER")
     */
    private String action;
    
    /**
     * Detailed description of the action performed
     */
    private String description;
    
    /**
     * IP address from which the action was performed
     */
    private String ipAddress;
    
    /**
     * User agent (browser/device) from which the action was performed
     */
    private String userAgent;
}
