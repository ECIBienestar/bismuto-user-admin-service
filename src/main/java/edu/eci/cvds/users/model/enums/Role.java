package edu.eci.cvds.users.model.enums;

/**
 * Enumeration representing the different roles users can have in the system.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public enum Role {
    ADMINISTRATOR,              // System administrator with full access
    MEDICAL_SECRETARY,          // Medical secretary
    MEDICAL_STAFF,              // Doctors, dentists, psychologists
    GENERAL_SERVICES_STAFF,     // General services staff
    PREFECT,                    // Supervisory role for specific areas
    STUDENT,                    // Regular university student
    TRAINER,                    // Gym or sports coach
    WELLNESS_STAFF,             // Staff members who manage wellness facilities
    MONITOR,                    // Student in charge of managing recreational rooms and equipment loans
    TEACHER                     // Teachers who manage classes and students
}
