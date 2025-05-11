package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.User;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.repository.UserRepository;
import edu.eci.cvds.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the UserService interface.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String id) {
        log.info("Retrieving user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));
        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));
                
        // Update basic user fields
        user.setFullName(dto.getFullName());
        user.setIdType(dto.getIdType());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        
        User savedUser = userRepository.save(user);
        log.info("User updated successfully: {}", savedUser.getId());
        return mapToDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(String id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException.create("User", id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public long deleteAllUsers() {
        log.warn("Deleting ALL users from the system - this operation is irreversible");
        
        long userCount = userRepository.count();
        userRepository.deleteAll();
        
        log.warn("Successfully deleted {} users from the system", userCount);
        
        return userCount;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(Role role) {
        log.info("Retrieving users with role: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::mapToDto)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        log.info("Retrieving user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsersByEmail(String emailPartial) {
        log.info("Searching users with email containing: {}", emailPartial);
        return userRepository.findByEmailContainingIgnoreCase(emailPartial).stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Maps a User entity to a UserResponseDTO.
     * 
     * @param user the user entity
     * @return the user DTO
     */
    private UserResponseDTO mapToDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .idType(user.getIdType())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
