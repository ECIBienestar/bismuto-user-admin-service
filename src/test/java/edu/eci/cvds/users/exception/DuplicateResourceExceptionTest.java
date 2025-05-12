package edu.eci.cvds.users.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateResourceExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Resource already exists";
        DuplicateResourceException exception = new DuplicateResourceException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testCreateMethod() {
        String resourceType = "User";
        String field = "email";
        String value = "example@eci.edu.co";

        DuplicateResourceException exception = DuplicateResourceException.create(resourceType, field, value);

        assertEquals("User with email 'example@eci.edu.co' already exists", exception.getMessage());
    }
}
