package edu.eci.cvds.users.model.enums;

/**
 * Enumeration of specialties for staff members.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-10
 */
public enum Specialty {
    // Medical specialties
    GENERAL_MEDICINE,      // Medicina General
    DENTISTRY,             // Odontología
    PSYCHOLOGY,            // Psicología
    NUTRITION,             // Nutrición
    PHYSIOTHERAPY,         // Fisioterapia
    NURSING,               // Enfermería
    
    // Trainer specialties
    FITNESS_COACH,         // Entrenador de fitness
    YOGA_INSTRUCTOR,       // Instructor de yoga
    PILATES_INSTRUCTOR,    // Instructor de pilates
    CARDIO_COACH,          // Entrenador cardiovascular
    STRENGTH_COACH,        // Entrenador de fuerza
    SWIMMING_COACH,        // Entrenador de natación
    SOCCER_COACH,          // Entrenador de fútbol
    BASKETBALL_COACH,      // Entrenador de baloncesto
    VOLLEYBALL_COACH,      // Entrenador de voleibol
    DANCE_INSTRUCTOR,      // Instructor de baile
    
    // Other specialties
    WELLNESS_COORDINATOR,  // Coordinador de bienestar
    ADMINISTRATIVE,        // Administrativo
    OTHER                  // Otra especialidad
}
