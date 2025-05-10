package edu.eci.cvds.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * Results in HTTP 409 Conflict response.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new duplicate resource exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    /**
     * Creates a duplicate resource exception for a specific resource type and field.
     * 
     * @param resourceType the type of resource
     * @param field the field that is duplicated
     * @param value the value that is duplicated
     * @return a new duplicate resource exception
     */
    public static DuplicateResourceException create(String resourceType, String field, Object value) {
        return new DuplicateResourceException(
                String.format("%s with %s '%s' already exists", resourceType, field, value));
    }
}
