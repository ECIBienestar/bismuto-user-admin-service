package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.UserActivityLogDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.User;
import edu.eci.cvds.users.model.UserActivityLog;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.repository.StudentRepository;
import edu.eci.cvds.users.repository.UserActivityLogRepository;
import edu.eci.cvds.users.repository.UserRepository;
import edu.eci.cvds.users.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAdminService interface.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UserActivityLogRepository logRepository;

    @Override
    @Transactional
    public UserResponseDTO changeUserStatus(String id, boolean active) {
        log.info("Changing active status to {} for user with ID: {}", active, id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));
        
        user.setActive(active);
        
        // Record activity
        logActivity(user, active ? "ACTIVATE_USER" : "DEACTIVATE_USER", 
                    "User " + (active ? "activated" : "deactivated"));
        
        User savedUser = userRepository.save(user);
        log.info("User status updated successfully: {}", savedUser.getId());
        
        return mapToDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO changeUserRole(String id, String roleName) {
        log.info("Changing role to {} for user with ID: {}", roleName, id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));
        
        // Validate role
        Role role;
        try {
            role = Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw BadRequestException.invalidField("role", roleName);
        }
        
        // Check if user type is compatible with new role
        if ((user instanceof edu.eci.cvds.users.model.Student && role != Role.STUDENT) ||
            (user instanceof edu.eci.cvds.users.model.Staff && role == Role.STUDENT)) {
            throw new BadRequestException("Cannot change user type. Student users can only have STUDENT role.");
        }
        
        String oldRole = user.getRole().name();
        user.setRole(role);
        
        // Record activity
        logActivity(user, "CHANGE_ROLE", 
                    "User role changed from " + oldRole + " to " + role.name());
        
        User savedUser = userRepository.save(user);
        log.info("User role updated successfully: {}", savedUser.getId());
        
        return mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserActivityLogDTO> getUserActivityLogs(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Retrieving user activity logs");
        
        List<UserActivityLog> logs;
        
        if (userId != null) {
            if (startDate != null && endDate != null) {
                logs = logRepository.findByUserIdAndTimestampBetweenOrderByTimestampDesc(userId, startDate, endDate);
            } else if (startDate != null) {
                logs = logRepository.findByUserIdAndTimestampAfterOrderByTimestampDesc(userId, startDate);
            } else if (endDate != null) {
                logs = logRepository.findByUserIdAndTimestampBeforeOrderByTimestampDesc(userId, endDate);
            } else {
                logs = logRepository.findByUserIdOrderByTimestampDesc(userId);
            }
        } else {
            if (startDate != null && endDate != null) {
                logs = logRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate);
            } else if (startDate != null) {
                logs = logRepository.findByTimestampAfterOrderByTimestampDesc(startDate);
            } else if (endDate != null) {
                logs = logRepository.findByTimestampBeforeOrderByTimestampDesc(endDate);
            } else {
                logs = logRepository.findAll();
            }
        }
        
        return logs.stream()
                .map(this::mapToLogDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserStatistics() {
        log.info("Retrieving user statistics");
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Count users by role
        Map<Role, Long> usersByRole = userRepository.countByRoleGroupByRole().stream()
                .collect(Collectors.toMap(
                    result -> Role.valueOf(result[0].toString()),
                    result -> (Long) result[1]
                ));
        
        // Count active/inactive users
        long activeUsers = userRepository.countByActiveTrue();
        long inactiveUsers = userRepository.countByActiveFalse();
        
        // Count students by program
        Map<String, Long> studentsByProgram = studentRepository.countByProgramGroupByProgram().stream()
                .collect(Collectors.toMap(
                    result -> result[0].toString(),
                    result -> (Long) result[1]
                ));
        
        // Count students by semester
        Map<Integer, Long> studentsBySemester = studentRepository.countBySemesterGroupBySemester().stream()
                .collect(Collectors.toMap(
                    result -> (Integer) result[0],
                    result -> (Long) result[1]
                ));
        
        // Add statistics to map
        statistics.put("totalUsers", userRepository.count());
        statistics.put("usersByRole", usersByRole);
        statistics.put("activeUsers", activeUsers);
        statistics.put("inactiveUsers", inactiveUsers);
        statistics.put("studentsByProgram", studentsByProgram);
        statistics.put("studentsBySemester", studentsBySemester);
        
        return statistics;
    }

    @Override
    @Transactional
    public void deleteUserWithOptions(String id, boolean softDelete, boolean deleteAssociatedData) {
        log.info("Deleting user with ID: {} (softDelete: {}, deleteAssociatedData: {})", 
                 id, softDelete, deleteAssociatedData);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));
        
        if (softDelete) {
            // Soft delete - mark as inactive
            user.setActive(false);
            userRepository.save(user);
            
            // Record activity
            logActivity(user, "SOFT_DELETE", "User marked as inactive");
            
            log.info("User soft-deleted successfully: {}", id);
        } else {
            // Hard delete
            if (deleteAssociatedData) {
                // Delete associated logs
                logRepository.deleteByUserId(id);
            }
            
            // Record deletion activity (will be deleted with user if deleteAssociatedData=true)
            logActivity(user, "DELETE_USER", "User deleted from system");
            
            // Delete user
            userRepository.delete(user);
            
            log.info("User deleted successfully: {}", id);
        }
    }
    
    /**
     * Records a user activity.
     * 
     * @param user the user performing the action
     * @param action the action performed
     * @param description the description of the action
     */
    private void logActivity(User user, String action, String description) {
        UserActivityLog log = UserActivityLog.builder()
                .user(user)
                .action(action)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
        
        logRepository.save(log);
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
                .build();
    }
    
    /**
     * Maps a UserActivityLog entity to a UserActivityLogDTO.
     * 
     * @param log the log entity
     * @return the log DTO
     */
    private UserActivityLogDTO mapToLogDto(UserActivityLog log) {
        return UserActivityLogDTO.builder()
                .logId(log.getLogId())
                .userId(log.getUser().getId())
                .userFullName(log.getUser().getFullName())
                .timestamp(log.getTimestamp())
                .action(log.getAction())
                .description(log.getDescription())
                .build();
    }
}
