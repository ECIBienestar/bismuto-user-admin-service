package edu.eci.cvds.users.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    @Test
    void whenValid_thenNoViolations() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setId("ID1");
        dto.setIdType("CC");
        dto.setFullName("Name");
        dto.setPhone(123);
        dto.setEmail("e@e.com");
        dto.setStudentCode("SC1");
        dto.setProgram("Prog");
        dto.setBirthDate(LocalDate.now());
        dto.setAddress("Addr");
        dto.setEmergencyContactId(54564L);

        Set<ConstraintViolation<StudentRequestDTO>> errs = validator.validate(dto);
        assertTrue(errs.isEmpty());
    }

    @Test
    void whenBlankFullName_thenViolation() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFullName("");  // inválido
        Set<ConstraintViolation<StudentRequestDTO>> errs = validator.validate(dto);
        assertFalse(errs.isEmpty());
    }
}
