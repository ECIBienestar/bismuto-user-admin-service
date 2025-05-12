package edu.eci.cvds.users.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserActivityLogTest {

    // Clase concreta para testing que extiende User
    private static class TestUser extends User {
        public TestUser(String id) {
            this.setId(id);
        }
    }

    @Test
    void testConstructorWithAllFields() {
        TestUser user = new TestUser("user123");
        LocalDateTime timestamp = LocalDateTime.now();
        String action = "LOGIN";
        String description = "User logged into the system";

        UserActivityLog log = new UserActivityLog("123", user, timestamp, action, description);

        assertEquals("123", log.getLogId());
        assertEquals(user, log.getUser());
        assertEquals(timestamp, log.getTimestamp());
        assertEquals(action, log.getAction());
        assertEquals(description, log.getDescription());
    }

    @Test
    void testCustomConstructor() {
        TestUser user = new TestUser("user123");
        String action = "LOGOUT";
        String description = "User logged out";

        UserActivityLog log = new UserActivityLog(user, action, description);

        assertNotNull(log.getLogId());
        assertEquals(user, log.getUser());
        assertNotNull(log.getTimestamp());
        assertEquals(action, log.getAction());
        assertEquals(description, log.getDescription());
    }

    @Test
    void testCustomConstructorWithNullDescription() {
        TestUser user = new TestUser("user123");
        String action = "PROFILE_UPDATE";

        UserActivityLog log = new UserActivityLog(user, action, null);

        assertNotNull(log.getLogId());
        assertEquals(user, log.getUser());
        assertNotNull(log.getTimestamp());
        assertEquals(action, log.getAction());
        assertNull(log.getDescription());
    }

    @Test
    void testCreateFactoryMethod() {
        TestUser user = new TestUser("user123");
        String action = "PASSWORD_CHANGE";

        UserActivityLog log = UserActivityLog.create(user, action);

        assertNotNull(log.getLogId());
        assertEquals(user, log.getUser());
        assertNotNull(log.getTimestamp());
        assertEquals(action, log.getAction());
        assertNull(log.getDescription());
    }

    @Test
    void testBuilder() {
        TestUser user = new TestUser("user123");
        LocalDateTime timestamp = LocalDateTime.now();
        String action = "SYSTEM_EVENT";
        String description = "System maintenance";

        UserActivityLog log = UserActivityLog.builder()
                .logId("456")
                .user(user)
                .timestamp(timestamp)
                .action(action)
                .description(description)
                .build();

        assertEquals("456", log.getLogId());
        assertEquals(user, log.getUser());
        assertEquals(timestamp, log.getTimestamp());
        assertEquals(action, log.getAction());
        assertEquals(description, log.getDescription());
    }

    @Test
    void testSetters() {
        TestUser user = new TestUser("user123");
        LocalDateTime timestamp = LocalDateTime.now();
        String action = "ACCOUNT_CREATION";
        String description = "New user account created";

        UserActivityLog log = new UserActivityLog();
        log.setLogId("789");
        log.setUser(user);
        log.setTimestamp(timestamp);
        log.setAction(action);
        log.setDescription(description);

        assertEquals("789", log.getLogId());
        assertEquals(user, log.getUser());
        assertEquals(timestamp, log.getTimestamp());
        assertEquals(action, log.getAction());
        assertEquals(description, log.getDescription());
    }

    @Test
    void testNoArgsConstructor() {
        UserActivityLog log = new UserActivityLog();

        assertNull(log.getLogId());
        assertNull(log.getUser());
        assertNull(log.getTimestamp());
        assertNull(log.getAction());
        assertNull(log.getDescription());
    }

    @Test
    void testPrePersistSetsIdWhenNull() {
        UserActivityLog log = UserActivityLog.builder()
                .user(new TestUser("user123"))
                .action("LOGIN")
                .build();
        assertNull(log.getLogId());

        log.prePersist();

        assertNotNull(log.getLogId());
        try {
            UUID.fromString(log.getLogId());
        } catch (IllegalArgumentException e) {
            fail("logId should be a valid UUID");
        }
    }

    @Test
    void testPrePersistSetsTimestampWhenNull() {
        UserActivityLog log = UserActivityLog.builder()
                .user(new TestUser("user123"))
                .action("LOGIN")
                .build();
        assertNull(log.getTimestamp());

        log.prePersist();

        assertNotNull(log.getTimestamp());
        assertTrue(log.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)) ||
                log.getTimestamp().isEqual(LocalDateTime.now()));
    }


    @Test
    void testPrePersistDoesNotOverrideExistingTimestamp() {
        LocalDateTime existingTime = LocalDateTime.now().minusHours(1);
        UserActivityLog log = UserActivityLog.builder()
                .timestamp(existingTime)
                .user(new TestUser("user123"))
                .action("LOGIN")
                .build();

        log.prePersist();

        assertEquals(existingTime, log.getTimestamp());
    }

    @Test
    void testPrePersistSetsBothWhenBothNull() {
        UserActivityLog log = UserActivityLog.builder()
                .user(new TestUser("user123"))
                .action("LOGIN")
                .build();
        assertNull(log.getLogId());
        assertNull(log.getTimestamp());

        log.prePersist();

        assertNotNull(log.getLogId());
        assertNotNull(log.getTimestamp());
    }

    @Test
    void testPrePersistDoesNotOverrideExistingId() {
        String existingId = "custom-id-123";
        UserActivityLog log = UserActivityLog.builder()
                .logId(existingId)
                .user(new TestUser("user123"))
                .action("LOGIN")
                .build();

        log.prePersist();

        assertEquals(existingId, log.getLogId());
    }
}