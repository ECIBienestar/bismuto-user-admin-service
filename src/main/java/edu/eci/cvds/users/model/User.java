package edu.eci.cvds.users.model;

import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.model.enums.IdType;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Base abstract user entity that represents common attributes for all system users.
 * This class serves as the parent class for more specific user types like Student and Staff.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public abstract class User {
    
    @Id
    @Column(length = 15)
    @NotBlank(message = "ID cannot be blank")
    @Size(min = 5, max = 15, message = "ID must be between 5 and 15 characters")
    private String id; // national ID

    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", nullable = false, length = 20)
    private IdType idType;

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Phone number cannot be blank")
    // @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone must be a valid number with 8-15 digits, optionally starting with '+'")
    private String phone;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email must be at most 50 characters")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean active = true;
}
