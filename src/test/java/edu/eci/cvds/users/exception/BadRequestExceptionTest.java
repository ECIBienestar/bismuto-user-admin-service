package edu.eci.cvds.users.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "This is a bad request";
        BadRequestException exception = new BadRequestException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Bad request with cause";
        Throwable cause = new IllegalArgumentException("Invalid argument");
        BadRequestException exception = new BadRequestException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testInvalidFieldMethod() {
        String fieldName = "age";
        Object invalidValue = -5;
        BadRequestException exception = BadRequestException.invalidField(fieldName, invalidValue);

        assertEquals("Invalid value '-5' for field 'age'", exception.getMessage());
    }
}
