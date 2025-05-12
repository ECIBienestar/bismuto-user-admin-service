package edu.eci.cvds.users.exception;

import java.lang.reflect.Method;
import edu.eci.cvds.users.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.core.MethodParameter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void testHandleBadRequest() {
        BadRequestException ex = new BadRequestException("Invalid input");
        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(ex, mockRequest);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFound(ex, mockRequest);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Not found", response.getBody().getMessage());
    }

    @Test
    void testHandleAccessDenied() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");
        ResponseEntity<ErrorResponse> response = handler.handleAccessDenied(ex, mockRequest);

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Forbidden", response.getBody().getError());
        assertEquals("You don't have permission to access this resource", response.getBody().getMessage());
    }

    @Test
    void testHandleDuplicateResource() {
        DuplicateResourceException ex = DuplicateResourceException.create("User", "email", "test@example.com");
        ResponseEntity<ErrorResponse> response = handler.handleDuplicateResource(ex, mockRequest);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Conflict", response.getBody().getError());
        assertEquals("User with email 'test@example.com' already exists", response.getBody().getMessage());
    }

    @Test
    void testHandleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Integer.class, "number", null, new IllegalArgumentException("Type mismatch"));

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentTypeMismatch(ex, mockRequest);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Bad Request", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("parameter 'number' of value 'abc'"));
    }

    @Test
    void testHandleAllUncaughtExceptions() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<ErrorResponse> response = handler.handleAllUncaughtExceptions(ex, mockRequest);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("unexpected error occurred"));
    }

    @Test
    void testHandleMissingServletRequestParameter() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("id", "String");

        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/api/data");

        ResponseEntity<Object> response = handler.handleMissingServletRequestParameter(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

        ErrorResponse error = (ErrorResponse) response.getBody();

        assertNotNull(error);
        assertEquals(400, error.getStatus());
        assertEquals("Bad Request", error.getError());
        assertTrue(error.getMessage().contains("Required parameter 'id' is missing"));
        assertEquals("/api/data", error.getPath());
    }

    @Test
    void testHandleMethodArgumentNotValid_SingleError() throws NoSuchMethodException {
        FieldError fieldError = new FieldError("object", "username", "must not be blank");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodParameter parameter = new MethodParameter(
                String.class.getMethod("toString"), -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                parameter, bindingResult);

        HttpHeaders headers = new HttpHeaders();
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/api/users");

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                ex, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

        ErrorResponse body = (ErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals(422, body.getStatus());
        assertEquals("Validation Failed", body.getError());
        assertEquals("username: must not be blank", body.getMessage());
        assertEquals("/api/users", body.getPath());
    }
}