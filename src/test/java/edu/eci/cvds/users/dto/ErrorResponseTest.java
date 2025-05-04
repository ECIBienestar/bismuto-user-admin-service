package edu.eci.cvds.users.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void gettersAndSettersWork() {
        ErrorResponse err = new ErrorResponse();
        err.setStatus(400);
        err.setError("Bad Request");
        err.setMessage("Oops");
        err.setPath("/test");

        assertEquals(400, err.getStatus());
        assertEquals("Bad Request", err.getError());
        assertEquals("Oops", err.getMessage());
        assertEquals("/test", err.getPath());
    }
}
