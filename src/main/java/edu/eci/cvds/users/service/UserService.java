package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.model.enums.Role;

import java.util.List;

/**
 * Service interface for user management operations.
 * Provides methods for common user operations.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public interface UserService {
    
    /**
     * Retrieves all users in the system.
     * 
     * @return list of all users
     */
    List<UserResponseDTO> getAllUsers();
    
    /**
     * Retrieves a user by their ID.
     * 
     * @param id the user ID
     * @return the user with the given ID
     */
    UserResponseDTO getUserById(String id);
    
    /**
     * Updates a user's basic information.
     * 
     * @param id the user ID
     * @param dto the updated user data
     * @return the updated user
     */
    UserResponseDTO updateUser(String id, UserRequestDTO dto);
    
    /**
     * Deletes a user by their ID.
     * 
     * @param id the user ID
     */
    void deleteUserById(String id);

    /**
     * Deletes all users from the system.
     * This operation is irreversible and should be used with caution.
     * 
     * @return number of users deleted
     */
    long deleteAllUsers();
    
    /**
     * Retrieves all users with a specific role.
     * 
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    List<UserResponseDTO> getUsersByRole(Role role);

    /**
     * Retrieves a user by email.
     * 
     * @param email the exact email to search for
     * @return the user with the given email
     */
    UserResponseDTO getUserByEmail(String email);
    
    /**
     * Searches for users with email containing the given text.
     * 
     * @param emailPartial partial email to search for
     * @return list of users with matching email
     */
    List<UserResponseDTO> searchUsersByEmail(String emailPartial);
}
