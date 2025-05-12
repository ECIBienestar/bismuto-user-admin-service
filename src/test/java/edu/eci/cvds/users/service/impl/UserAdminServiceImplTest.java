package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.UserActivityLogDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.TestUser;
import edu.eci.cvds.users.model.User;
import edu.eci.cvds.users.model.UserActivityLog;
import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.repository.StudentRepository;
import edu.eci.cvds.users.repository.UserActivityLogRepository;
import edu.eci.cvds.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserActivityLogRepository logRepository;

    @InjectMocks
    private UserAdminServiceImpl userAdminService;

    private TestUser testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new TestUser(
                "123",
                IdType.CC,
                "Test User",
                "1234567890",
                "test@test.com",
                Role.STUDENT,
                "password123"
        );
    }

    @Test
    void changeUserStatusShouldActivateUser() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(TestUser.class))).thenReturn(testUser);

        UserResponseDTO response = userAdminService.changeUserStatus("123", true);

        assertTrue(testUser.isActive());
        assertEquals("123", response.getId());
        verify(logRepository).save(any(UserActivityLog.class));
    }

    @Test
    void changeUserStatusShouldDeactivateUser() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(TestUser.class))).thenReturn(testUser);

        UserResponseDTO response = userAdminService.changeUserStatus("123", false);

        assertFalse(testUser.isActive());
        assertEquals("123", response.getId());
        verify(logRepository).save(any(UserActivityLog.class));
    }

    @Test
    void changeUserStatusShouldThrowWhenUserNotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userAdminService.changeUserStatus("999", true);
        });
    }

    @Test
    void changeUserRoleShouldUpdateRole() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(TestUser.class))).thenReturn(testUser);

        UserResponseDTO response = userAdminService.changeUserRole("123", "STUDENT");

        assertEquals(Role.STUDENT, response.getRole());
        verify(logRepository).save(any(UserActivityLog.class));
    }

    @Test
    void changeUserRoleShouldThrowForInvalidRole() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));

        assertThrows(BadRequestException.class, () -> {
            userAdminService.changeUserRole("123", "INVALID_ROLE");
        });
    }

    @Test
    void getUserStatisticsShouldReturnCompleteStats() {
        // Mock user counts
        Object[] roleCount1 = new Object[]{"STUDENT", 50L};
        Object[] roleCount2 = new Object[]{"ADMINISTRATOR", 5L};
        when(userRepository.countByRoleGroupByRole()).thenReturn(Arrays.asList(roleCount1, roleCount2));
        when(userRepository.countByActiveTrue()).thenReturn(52L);
        when(userRepository.countByActiveFalse()).thenReturn(3L);
        when(userRepository.count()).thenReturn(55L);

        // Mock student counts
        Object[] programCount1 = new Object[]{"Computer Science", 30L};
        Object[] programCount2 = new Object[]{"Electrical Engineering", 20L};
        when(studentRepository.countByProgramGroupByProgram()).thenReturn(Arrays.asList(programCount1, programCount2));

        Object[] semesterCount1 = new Object[]{3, 15L};
        Object[] semesterCount2 = new Object[]{4, 20L};
        Object[] semesterCount3 = new Object[]{5, 15L};
        when(studentRepository.countBySemesterGroupBySemester()).thenReturn(Arrays.asList(semesterCount3, semesterCount2, semesterCount1));

        Map<String, Object> stats = userAdminService.getUserStatistics();

        assertEquals(55L, stats.get("totalUsers"));
        assertEquals(52L, stats.get("activeUsers"));
        assertEquals(3L, stats.get("inactiveUsers"));

        @SuppressWarnings("unchecked")
        Map<Role, Long> roles = (Map<Role, Long>) stats.get("usersByRole");
        assertEquals(50L, roles.get(Role.STUDENT));
        assertEquals(5L, roles.get(Role.ADMINISTRATOR));

        @SuppressWarnings("unchecked")
        Map<String, Long> programs = (Map<String, Long>) stats.get("studentsByProgram");
        assertEquals(30L, programs.get("Computer Science"));
        assertEquals(20L, programs.get("Electrical Engineering"));

        @SuppressWarnings("unchecked")
        Map<Integer, Long> semesters = (Map<Integer, Long>) stats.get("studentsBySemester");
        assertEquals(15L, semesters.get(3));
        assertEquals(20L, semesters.get(4));
    }

    @Test
    void deleteUserWithOptionsShouldSoftDeleteWhenRequested() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));

        userAdminService.deleteUserWithOptions("123", true, false);

        assertFalse(testUser.isActive());
        verify(userRepository).save(testUser);
        verify(logRepository).save(any(UserActivityLog.class));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUserWithOptionsShouldHardDeleteWhenRequested() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));

        userAdminService.deleteUserWithOptions("123", false, true);

        verify(userRepository).delete(testUser);
        verify(logRepository).deleteByUserId("123");
        verify(logRepository).save(any(UserActivityLog.class));
    }

    @Test
    void deleteUserWithOptionsShouldNotDeleteLogsWhenNotRequested() {
        when(userRepository.findById("123")).thenReturn(Optional.of(testUser));

        userAdminService.deleteUserWithOptions("123", false, false);

        verify(userRepository).delete(testUser);
        verify(logRepository, never()).deleteByUserId(anyString());
        verify(logRepository).save(any(UserActivityLog.class));
    }

    @Test
    void getUserActivityLogsShouldFilterByStartDateOnly() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now().minusDays(3), "ACTION", "DETAILS");

        when(logRepository.findByTimestampAfterOrderByTimestampDesc(start)).thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs(null, start, null);

        assertEquals(1, result.size());
        verify(logRepository).findByTimestampAfterOrderByTimestampDesc(start);
    }

    @Test
    void getUserActivityLogsShouldFilterByUserIdAndDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now().minusDays(3), "ACTION", "DETAILS");

        when(logRepository.findByUserIdAndTimestampBetweenOrderByTimestampDesc("123", start, end))
                .thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs("123", start, end);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getUserId());
        verify(logRepository).findByUserIdAndTimestampBetweenOrderByTimestampDesc("123", start, end);
    }

    @Test
    void getUserActivityLogsShouldFilterByDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now().minusDays(3), "ACTION", "DETAILS");

        when(logRepository.findByTimestampBetweenOrderByTimestampDesc(start, end)).thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs(null, start, end);

        assertEquals(1, result.size());
        verify(logRepository).findByTimestampBetweenOrderByTimestampDesc(start, end);
    }

    @Test
    void getUserActivityLogsShouldFilterByUserId() {
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now(), "ACTION", "DETAILS");

        when(logRepository.findByUserIdOrderByTimestampDesc("123")).thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs("123", null, null);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getUserId());
        verify(logRepository).findByUserIdOrderByTimestampDesc("123");
    }

    void getUserActivityLogsShouldReturnAllLogsWhenNoFilters() {
        User user1 = new TestUser("123", IdType.CC, "User 1", "123456", "user1@test.com", Role.STUDENT, "pass");
        User user2 = new TestUser("456", IdType.CC, "User 2", "654321", "user2@test.com", Role.ADMINISTRATOR, "pass");

        UserActivityLog log1 = new UserActivityLog("123", user1, LocalDateTime.now().minusDays(1), "ACTION", "DETAILS");
        UserActivityLog log2 = new UserActivityLog("456", user2, LocalDateTime.now(), "ACTION", "DETAILS");

        when(logRepository.findAll()).thenReturn(Arrays.asList(log1, log2));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs(null, null, null);

        assertEquals(2, result.size());
        verify(logRepository).findAll();
    }

    @Test
    void getUserActivityLogsShouldReturnEmptyListWhenNoResults() {
        when(logRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs(null, null, null);

        assertTrue(result.isEmpty());
        verify(logRepository).findAll();
    }

    @Test
    void getUserActivityLogsShouldFilterByEndDateOnly() {
        LocalDateTime end = LocalDateTime.now();
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now().minusDays(3), "ACTION", "DETAILS");

        when(logRepository.findByTimestampBeforeOrderByTimestampDesc(end)).thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs(null, null, end);

        assertEquals(1, result.size());
        verify(logRepository).findByTimestampBeforeOrderByTimestampDesc(end);
    }

    @Test
    void getUserActivityLogsShouldFilterByUserIdAndStartDate() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now().minusDays(3), "ACTION", "DETAILS");

        when(logRepository.findByUserIdAndTimestampAfterOrderByTimestampDesc("123", start))
                .thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs("123", start, null);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getUserId());
        verify(logRepository).findByUserIdAndTimestampAfterOrderByTimestampDesc("123", start);
    }

    @Test
    void getUserActivityLogsShouldFilterByUserIdAndEndDate() {
        LocalDateTime end = LocalDateTime.now();
        User user = new TestUser("123", IdType.CC, "Test User", "123456", "test@test.com", Role.STUDENT, "pass");
        UserActivityLog log = new UserActivityLog("123", user, LocalDateTime.now().minusDays(3), "ACTION", "DETAILS");

        when(logRepository.findByUserIdAndTimestampBeforeOrderByTimestampDesc("123", end))
                .thenReturn(Collections.singletonList(log));

        List<UserActivityLogDTO> result = userAdminService.getUserActivityLogs("123", null, end);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getUserId());
        verify(logRepository).findByUserIdAndTimestampBeforeOrderByTimestampDesc("123", end);
    }

    @Test
    void changeUserRoleShouldAllowStudentToKeepStudentRole() {
        User studentUser = new TestUser("123", IdType.CC, "Student", "123456", "student@test.com", Role.STUDENT, "pass");
        when(userRepository.findById("123")).thenReturn(Optional.of(studentUser));
        when(userRepository.save(any(User.class))).thenReturn(studentUser);

        UserResponseDTO response = userAdminService.changeUserRole("123", "STUDENT");

        assertEquals(Role.STUDENT, response.getRole());
        verify(userRepository).save(studentUser);
    }
}