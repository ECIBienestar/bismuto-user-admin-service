package edu.eci.cvds.users.model.enums;

/**
 * Enumeration representing the different types of identification documents used in Colombia.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public enum IdType {
    ANI,   // Archivo Nacional de Identificación - National Identification Archive
    CC,    // Cédula de Ciudadanía - Colombian National ID for adult citizens
    NIP,   // Número de Identificación Personal - Personal Identification Number
    NUIP,  // Número de Identificación Universal - Universal Identification Number
    PA,    // Pasaporte - Passport
    RC,    // Registro Civil - Civil Registry (for children)
    TI     // Tarjeta de Identidad - Identity Card (for adolescents)
}