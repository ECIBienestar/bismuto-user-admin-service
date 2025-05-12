package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.IdType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BaseUserDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructor() {
        BaseUserDTO user = new BaseUserDTO();
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getIdType());
        assertNull(user.getFullName());
        assertNull(user.getPhone());
        assertNull(user.getEmail());
    }

    @Test
    void testAllArgsConstructor() {
        BaseUserDTO user = new BaseUserDTO(
                "123456789",
                IdType.CC,
                "John Doe",
                "+573001234567",
                "john.doe@example.com"
        );

        assertEquals("123456789", user.getId());
        assertEquals(IdType.CC, user.getIdType());
        assertEquals("John Doe", user.getFullName());
        assertEquals("+573001234567", user.getPhone());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testBuilder() {
        BaseUserDTO user = BaseUserDTO.builder()
                .id("987654321")
                .idType(IdType.TI)
                .fullName("Jane Smith")
                .phone("+573009876543")
                .email("jane.smith@example.com")
                .build();

        assertEquals("987654321", user.getId());
        assertEquals(IdType.TI, user.getIdType());
        assertEquals("Jane Smith", user.getFullName());
        assertEquals("+573009876543", user.getPhone());
        assertEquals("jane.smith@example.com", user.getEmail());
    }

    @Test
    void testSettersAndGetters() {
        BaseUserDTO user = new BaseUserDTO();

        user.setId("1122334455");
        user.setIdType(IdType.TI);
        user.setFullName("Test User");
        user.setPhone("+57300112233");
        user.setEmail("test.user@example.com");

        assertEquals("1122334455", user.getId());
        assertEquals(IdType.TI, user.getIdType());
        assertEquals("Test User", user.getFullName());
        assertEquals("+57300112233", user.getPhone());
        assertEquals("test.user@example.com", user.getEmail());
    }

    @Test
    void testIdValidation() {
        BaseUserDTO user = BaseUserDTO.builder()
                .id("") // Blank ID
                .idType(IdType.CC)
                .fullName("Valid Name")
                .phone("+573001234567")
                .email("valid@example.com")
                .build();

        Set<ConstraintViolation<BaseUserDTO>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("ID cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testFullNameValidation() {
        BaseUserDTO user = BaseUserDTO.builder()
                .id("123456789")
                .idType(IdType.CC)
                .fullName("")
                .phone("+573001234567")
                .email("valid@example.com")
                .build();

        Set<ConstraintViolation<BaseUserDTO>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Full name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testPhoneValidation() {
        BaseUserDTO user = BaseUserDTO.builder()
                .id("123456789")
                .idType(IdType.CC)
                .fullName("Valid Name")
                .phone("")
                .email("valid@example.com")
                .build();

        Set<ConstraintViolation<BaseUserDTO>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Phone number cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testEmailValidation() {
        BaseUserDTO blankEmail = BaseUserDTO.builder()
                .id("123456789")
                .idType(IdType.CC)
                .fullName("Valid Name")
                .phone("+573001234567")
                .email("")
                .build();

        Set<ConstraintViolation<BaseUserDTO>> blankViolations = validator.validate(blankEmail);
        assertEquals(1, blankViolations.size());
        assertEquals("Email cannot be blank", blankViolations.iterator().next().getMessage());

        BaseUserDTO invalidEmail = BaseUserDTO.builder()
                .id("123456789")
                .idType(IdType.CC)
                .fullName("Valid Name")
                .phone("+573001234567")
                .email("invalid-email")
                .build();

        Set<ConstraintViolation<BaseUserDTO>> formatViolations = validator.validate(invalidEmail);
        assertEquals(1, formatViolations.size());
        assertEquals("Email must be valid", formatViolations.iterator().next().getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        BaseUserDTO user1 = BaseUserDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("John Doe")
                .phone("+573001234567")
                .email("john@example.com")
                .build();

        BaseUserDTO user2 = BaseUserDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("John Doe")
                .phone("+573001234567")
                .email("john@example.com")
                .build();

        BaseUserDTO user3 = BaseUserDTO.builder()
                .id("456")
                .idType(IdType.CC)
                .fullName("John Doe")
                .phone("+573001234567")
                .email("john@example.com")
                .build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        BaseUserDTO user = BaseUserDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("John Doe")
                .phone("+573001234567")
                .email("john@example.com")
                .build();

        String toString = user.toString();
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("CC"));
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("+573001234567"));
        assertTrue(toString.contains("john@example.com"));
    }
}