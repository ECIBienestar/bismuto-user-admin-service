package edu.eci.cvds.users.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validUserRequest_noViolations() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setId("U1");
        dto.setIdType("CC");
        dto.setFullName("User");
        dto.setPhone(999);
        dto.setEmail("u@u.com");
        dto.setRole("TRAINER");

        Set<ConstraintViolation<UserRequestDTO>> errs = validator.validate(dto);
        assertTrue(errs.isEmpty());
    }

    @Test
    void invalidEmail_thenViolation() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("bad-email");
        Set<ConstraintViolation<UserRequestDTO>> errs = validator.validate(dto);
        assertTrue(errs.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
}
