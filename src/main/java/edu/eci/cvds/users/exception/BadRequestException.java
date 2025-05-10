package edu.eci.cvds.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the request is invalid.
 * Results in HTTP 400 Bad Request response.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new bad request exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public BadRequestException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new bad request exception with a detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a bad request exception for an invalid field value.
     * 
     * @param fieldName the name of the invalid field
     * @param value the invalid value
     * @return a new bad request exception
     */
    public static BadRequestException invalidField(String fieldName, Object value) {
        return new BadRequestException(
                String.format("Invalid value '%s' for field '%s'", value, fieldName));
    }
}
