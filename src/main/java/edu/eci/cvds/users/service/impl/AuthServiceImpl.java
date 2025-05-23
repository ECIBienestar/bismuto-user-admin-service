package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.AuthResponseDTO;
import edu.eci.cvds.users.dto.CredentialsDTO;
import edu.eci.cvds.users.dto.PasswordResetDTO;
import edu.eci.cvds.users.dto.PasswordUpdateDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.User;
import edu.eci.cvds.users.repository.UserRepository;
import edu.eci.cvds.users.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO validateCredentials(CredentialsDTO credentials) {
        log.info("Validating credentials for: {}", credentials.getUsername());
        
        // Search for user by ID or email
        Optional<User> userOpt = userRepository.findById(credentials.getUsername());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(credentials.getUsername());
        }
        
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", credentials.getUsername());
            return AuthResponseDTO.builder()
                    .authenticated(false)
                    .message("User not found")
                    .build();
        }
        
        User user = userOpt.get();
        
        // Verify if user is active
        if (!user.isActive()) {
            log.warn("User is inactive: {}", user.getId());
            return AuthResponseDTO.builder()
                    .authenticated(false)
                    .message("User account is inactive")
                    .build();
        }
        
        // Verify password
        boolean passwordMatches = passwordEncoder.matches(
                credentials.getPassword(), user.getPassword());
        
        if (!passwordMatches) {
            log.warn("Invalid password for user: {}", user.getId());
            return AuthResponseDTO.builder()
                    .authenticated(false)
                    .message("Invalid credentials")
                    .build();
        }
        
        log.info("Authentication successful for user: {}", user.getId());
        return AuthResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .authenticated(true)
                .message("Authentication successful")
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDTO updatePassword(String userId, PasswordUpdateDTO passwordUpdate) {
        log.info("Updating password for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        // Verify current password
        boolean passwordMatches = passwordEncoder.matches(
                passwordUpdate.getCurrentPassword(), user.getPassword());
        
        if (!passwordMatches) {
            log.warn("Current password is incorrect for user: {}", userId);
            throw new BadRequestException("Current password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(passwordUpdate.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password updated successfully for user: {}", userId);
        return AuthResponseDTO.builder()
                .id(user.getId())
                .authenticated(true)
                .message("Password updated successfully")
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDTO resetPasswordByEmail(PasswordResetDTO passwordReset) {
        log.info("Resetting password for user with email: {}", passwordReset.getEmail());
        
        User user = userRepository.findByEmail(passwordReset.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + passwordReset.getEmail()));
        
        // Verify if user is active
        if (!user.isActive()) {
            log.warn("User is inactive: {}", user.getId());
            throw new BadRequestException("User account is inactive");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(passwordReset.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password reset successfully for user: {}", user.getId());
        return AuthResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .authenticated(true)
                .message("Password reset successfully")
                .build();
    }
}
