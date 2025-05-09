package edu.eci.cvds.users.model;

import edu.eci.cvds.users.model.enums.Program;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Entity representing a student in the university wellness system.
 * Extends the base User class with student-specific attributes.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SuperBuilder
public class Student extends User {
    
    @Column(name = "student_code", nullable = false, unique = true)
    @NotBlank(message = "Student code cannot be blank")
    @Size(max = 12, message = "Student code must be at most 12 characters")
    private String studentCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Program cannot be null")
    private Program program;

    @Column(nullable = false)
    @Min(value = 0, message = "Semester must be at least 1")
    @Max(value = 12, message = "Semester must be at most 12")
    private int semester;

    @Column(name = "birth_date", nullable = false)
    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emergency_contact_id")
    @NotNull(message = "Emergency contact cannot be null")
    private EmergencyContact emergencyContact;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 100, message = "Address must be at most 100 characters")
    private String address;
}
