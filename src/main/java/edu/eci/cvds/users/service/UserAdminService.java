package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.UserActivityLogDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for administrative operations on users.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public interface UserAdminService {
    
    /**
     * Changes the active status of a user.
     * 
     * @param id the user ID
     * @param active the new active status
     * @return the updated user
     */
    UserResponseDTO changeUserStatus(String id, boolean active);
    
    /**
     * Changes the role of a user.
     * 
     * @param id the user ID
     * @param role the new role
     * @return the updated user
     */
    UserResponseDTO changeUserRole(String id, String role);
    
    /**
     * Retrieves activity logs for users.
     * 
     * @param userId optional user ID to filter logs by user
     * @param startDate optional start date to filter logs by time range
     * @param endDate optional end date to filter logs by time range
     * @return list of activity logs
     */
    List<UserActivityLogDTO> getUserActivityLogs(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Retrieves statistical data about system users.
     * 
     * @return map of statistics
     */
    Map<String, Object> getUserStatistics();
    
    /**
     * Deletes a user with additional options.
     * 
     * @param id the user ID
     * @param softDelete whether to perform a soft delete
     * @param deleteAssociatedData whether to delete associated data
     */
    void deleteUserWithOptions(String id, boolean softDelete, boolean deleteAssociatedData);
}
