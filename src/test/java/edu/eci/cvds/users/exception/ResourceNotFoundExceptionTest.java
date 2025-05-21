package edu.eci.cvds.users.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Resource not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testCreateWithResourceTypeAndId() {
        String resourceType = "User";
        Object resourceId = 123;
        ResourceNotFoundException ex = ResourceNotFoundException.create(resourceType, resourceId);

        String expectedMessage = "User with ID '123' not found";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void testCreateWithDifferentResourceTypeAndId() {
        String resourceType = "Product";
        Object resourceId = "ABC123";
        ResourceNotFoundException ex = ResourceNotFoundException.create(resourceType, resourceId);

        String expectedMessage = "Product with ID 'ABC123' not found";
        assertEquals(expectedMessage, ex.getMessage());
    }
}
