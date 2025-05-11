package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.AuthResponseDTO;
import edu.eci.cvds.users.dto.CredentialsDTO;
import edu.eci.cvds.users.dto.PasswordUpdateDTO;

public interface AuthService {
    
    /**
     * Validates user credentials for authentication.
     * 
     * @param credentials the user credentials
     * @return authentication response
     */
    AuthResponseDTO validateCredentials(CredentialsDTO credentials);
    
    /**
     * Updates a user's password.
     * 
     * @param userId the user ID
     * @param passwordUpdate the password update data
     * @return authentication response
     */
    AuthResponseDTO updatePassword(String userId, PasswordUpdateDTO passwordUpdate);
}
