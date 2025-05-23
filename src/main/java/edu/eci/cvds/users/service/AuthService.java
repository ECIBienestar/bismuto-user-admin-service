package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.AuthResponseDTO;
import edu.eci.cvds.users.dto.CredentialsDTO;
import edu.eci.cvds.users.dto.PasswordResetDTO;
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

    /**
     * Resets a user's password using their email address.
     * 
     * @param passwordReset the password reset data containing email and new password
     * @return authentication response
     */
    AuthResponseDTO resetPasswordByEmail(PasswordResetDTO passwordReset);
}
