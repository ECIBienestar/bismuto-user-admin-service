package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.EmergencyContactDTO;
import edu.eci.cvds.users.dto.EmergencyContactRequestDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.EmergencyContact;
import edu.eci.cvds.users.repository.EmergencyContactRepository;
import edu.eci.cvds.users.repository.StudentRepository;
import edu.eci.cvds.users.service.EmergencyContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the EmergencyContactService interface.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private static final String EMERGENCY_CONTACT = "EmergencyContact";
    private final EmergencyContactRepository contactRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public EmergencyContactDTO createEmergencyContact(EmergencyContactRequestDTO dto) {
        log.info("Creating new emergency contact");
        
        EmergencyContact contact = EmergencyContact.builder()
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .idType(dto.getIdType())
                .idNumber(dto.getIdNumber())
                .relationship(dto.getRelationship())
                .build();
        
        EmergencyContact savedContact = contactRepository.save(contact);
        log.info("Emergency contact created successfully: {}", savedContact.getId());
        
        return mapToDto(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public EmergencyContactDTO getEmergencyContactById(Long id) {
        log.info("Retrieving emergency contact with ID: {}", id);
        
        EmergencyContact contact = contactRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create(EMERGENCY_CONTACT, id));
        
        return mapToDto(contact);
    }

    @Override
    @Transactional
    public EmergencyContactDTO updateEmergencyContact(Long id, EmergencyContactRequestDTO dto) {
        log.info("Updating emergency contact with ID: {}", id);
        
        EmergencyContact contact = contactRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create(EMERGENCY_CONTACT, id));
        
        // Update contact fields
        contact.setFullName(dto.getFullName());
        contact.setPhone(dto.getPhone());
        contact.setIdType(dto.getIdType());
        contact.setIdNumber(dto.getIdNumber());
        contact.setRelationship(dto.getRelationship());
        
        EmergencyContact savedContact = contactRepository.save(contact);
        log.info("Emergency contact updated successfully: {}", savedContact.getId());
        
        return mapToDto(savedContact);
    }

    @Override
    @Transactional
    public void deleteEmergencyContact(Long id) {
        log.info("Deleting emergency contact with ID: {}", id);
        
        if (!contactRepository.existsById(id)) {
            throw ResourceNotFoundException.create(EMERGENCY_CONTACT, id);
        }
        
        // Check if the contact is referenced by any student
        if (studentRepository.existsByEmergencyContactId(id)) {
            throw new BadRequestException("Cannot delete emergency contact that is referenced by students");
        }
        
        contactRepository.deleteById(id);
        log.info("Emergency contact deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmergencyContactDTO> getAllEmergencyContacts() {
        log.info("Retrieving all emergency contacts");
        
        return contactRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }
    
    /**
     * Maps an EmergencyContact entity to an EmergencyContactDTO.
     * 
     * @param contact the emergency contact entity
     * @return the emergency contact DTO
     */
    private EmergencyContactDTO mapToDto(EmergencyContact contact) {
        return EmergencyContactDTO.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .phone(contact.getPhone())
                .idType(contact.getIdType())
                .idNumber(contact.getIdNumber())
                .relationship(contact.getRelationship())
                .build();
    }
}
