package edu.eci.cvds.users.model;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Relationship;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyContactTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testBuilderAndGetters() {
        EmergencyContact contact = EmergencyContact.builder()
                .id(1L)
                .idNumber("1234567890")
                .idType(IdType.CC)
                .fullName("María García")
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        assertNotNull(contact);
        assertEquals(1L, contact.getId());
        assertEquals("1234567890", contact.getIdNumber());
        assertEquals(IdType.CC, contact.getIdType());
        assertEquals("María García", contact.getFullName());
        assertEquals("+573001234567", contact.getPhone());
        assertEquals(Relationship.MOTHER, contact.getRelationship());
    }

    @Test
    void testNoArgsConstructor() {
        EmergencyContact contact = new EmergencyContact();

        assertNotNull(contact);
        assertNull(contact.getId());
        assertNull(contact.getIdNumber());
        assertNull(contact.getIdType());
        assertNull(contact.getFullName());
        assertNull(contact.getPhone());
        assertNull(contact.getRelationship());
    }

    @Test
    void testAllArgsConstructor() {
        EmergencyContact contact = new EmergencyContact(
                1L, "1234567890", IdType.CC,
                "María García", "+573001234567", Relationship.MOTHER);

        assertNotNull(contact);
        assertEquals(1L, contact.getId());
        assertEquals("1234567890", contact.getIdNumber());
        assertEquals(IdType.CC, contact.getIdType());
        assertEquals("María García", contact.getFullName());
        assertEquals("+573001234567", contact.getPhone());
        assertEquals(Relationship.MOTHER, contact.getRelationship());
    }

    @Test
    void testGetFormattedContactInfo() {
        EmergencyContact contact = EmergencyContact.builder()
                .fullName("María García")
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        String formattedInfo = contact.getFormattedContactInfo();

        assertEquals("María García (MOTHER): +573001234567", formattedInfo);
    }

    @Test
    void testValidationValidContact() {
        EmergencyContact contact = EmergencyContact.builder()
                .idNumber("1234567890")
                .idType(IdType.CC)
                .fullName("María García")
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        Set<ConstraintViolation<EmergencyContact>> violations = validator.validate(contact);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationBlankIdNumber() {
        EmergencyContact contact = EmergencyContact.builder()
                .idNumber("")  // Blank
                .idType(IdType.CC)
                .fullName("María García")
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        Set<ConstraintViolation<EmergencyContact>> violations = validator.validate(contact);

        assertEquals(1, violations.size());
        assertEquals("ID number cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testValidationLongIdNumber() {
        EmergencyContact contact = EmergencyContact.builder()
                .idNumber("123456789012345678901")  // 21 chars
                .idType(IdType.CC)
                .fullName("María García")
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        Set<ConstraintViolation<EmergencyContact>> violations = validator.validate(contact);

        assertEquals(1, violations.size());
        assertEquals("ID number must be at most 20 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testValidationBlankFullName() {
        EmergencyContact contact = EmergencyContact.builder()
                .idNumber("1234567890")
                .idType(IdType.CC)
                .fullName("")  // Blank
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        Set<ConstraintViolation<EmergencyContact>> violations = validator.validate(contact);

        assertEquals(1, violations.size());
        assertEquals("Full name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testValidationLongFullName() {
        String longName = "a".repeat(101);  // 101 chars
        EmergencyContact contact = EmergencyContact.builder()
                .idNumber("1234567890")
                .idType(IdType.CC)
                .fullName(longName)
                .phone("+573001234567")
                .relationship(Relationship.MOTHER)
                .build();

        Set<ConstraintViolation<EmergencyContact>> violations = validator.validate(contact);

        assertEquals(1, violations.size());
        assertEquals("Full name must be at most 100 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testValidationBlankPhone() {
        EmergencyContact contact = EmergencyContact.builder()
                .idNumber("1234567890")
                .idType(IdType.CC)
                .fullName("María García")
                .phone("")  // Blank
                .relationship(Relationship.MOTHER)
                .build();

        Set<ConstraintViolation<EmergencyContact>> violations = validator.validate(contact);

        assertEquals(1, violations.size());
        assertEquals("Phone number cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testSetters() {
        EmergencyContact contact = new EmergencyContact();

        contact.setId(1L);
        contact.setIdNumber("1234567890");
        contact.setIdType(IdType.CC);
        contact.setFullName("María García");
        contact.setPhone("+573001234567");
        contact.setRelationship(Relationship.MOTHER);

        assertEquals(1L, contact.getId());
        assertEquals("1234567890", contact.getIdNumber());
        assertEquals(IdType.CC, contact.getIdType());
        assertEquals("María García", contact.getFullName());
        assertEquals("+573001234567", contact.getPhone());
        assertEquals(Relationship.MOTHER, contact.getRelationship());
    }
}