package edu.eci.cvds.users.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BaseUserDTOTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validBaseUser_noViolations() {
        BaseUserDTO dto = new BaseUserDTO();
        dto.setId("ID1");
        dto.setIdType("CC");
        dto.setFullName("Name");
        dto.setPhone(555);
        dto.setEmail("e@e.com");

        Set<ConstraintViolation<BaseUserDTO>> errs = validator.validate(dto);
        assertTrue(errs.isEmpty());
    }

    @Test
    void blankFullName_violation() {
        BaseUserDTO dto = new BaseUserDTO();
        dto.setFullName("");
        Set<ConstraintViolation<BaseUserDTO>> errs = validator.validate(dto);
        assertFalse(errs.isEmpty());
    }
}
