package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.EmergencyContactDTO;
import edu.eci.cvds.users.dto.EmergencyContactRequestDTO;

import java.util.List;

/**
 * Service interface for emergency contact operations.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public interface EmergencyContactService {
    
    /**
     * Creates a new emergency contact.
     * 
     * @param dto the emergency contact data
     * @return the created emergency contact
     */
    EmergencyContactDTO createEmergencyContact(EmergencyContactRequestDTO dto);
    
    /**
     * Retrieves an emergency contact by its ID.
     * 
     * @param id the emergency contact ID
     * @return the emergency contact with the given ID
     */
    EmergencyContactDTO getEmergencyContactById(Long id);
    
    /**
     * Updates an emergency contact's information.
     * 
     * @param id the emergency contact ID
     * @param dto the updated emergency contact data
     * @return the updated emergency contact
     */
    EmergencyContactDTO updateEmergencyContact(Long id, EmergencyContactRequestDTO dto);
    
    /**
     * Deletes an emergency contact by its ID.
     * 
     * @param id the emergency contact ID
     */
    void deleteEmergencyContact(Long id);
    
    /**
     * Retrieves all emergency contacts.
     * 
     * @return list of all emergency contacts
     */
    List<EmergencyContactDTO> getAllEmergencyContacts();
}
