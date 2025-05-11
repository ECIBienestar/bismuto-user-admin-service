package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.ScheduleEntryDTO;
import edu.eci.cvds.users.dto.StaffResponseDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.DuplicateResourceException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.ExternalScheduleEntry;
import edu.eci.cvds.users.model.Staff;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.model.enums.Specialty;
import edu.eci.cvds.users.repository.StaffRepository;
import edu.eci.cvds.users.repository.UserRepository;
import edu.eci.cvds.users.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the StaffService interface.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private static final String STAFF = "Staff";
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StaffResponseDTO createStaff(UserRequestDTO dto) {
        log.info("Creating new staff member with ID: {}", dto.getId());
        
        // Check if user with same ID or email already exists
        if (userRepository.existsById(dto.getId())) {
            throw DuplicateResourceException.create("User", "id", dto.getId());
        }
        
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw DuplicateResourceException.create("User", "email", dto.getEmail());
        }
        
        // Validate role
        Role role;
        try {
            role = Role.valueOf(dto.getRole().name());
        } catch (IllegalArgumentException e) {
            throw BadRequestException.invalidField("role", dto.getRole());
        }
        
        // Determine default specialty based on role
        Specialty defaultSpecialty = null;
        if (role == Role.MEDICAL_STAFF) {
            defaultSpecialty = Specialty.GENERAL_MEDICINE;
        } else if (role == Role.TRAINER) {
            defaultSpecialty = Specialty.FITNESS_COACH;
        }

        // Geberate default password if not provided
        String password = dto.getPassword() != null ? 
                dto.getPassword() : 
                generateDefaultPassword(dto);
        
        // Encrypt password
        String encodedPassword = passwordEncoder.encode(password);
        
        // Crear staff member
        Staff staff = Staff.builder()
                .id(dto.getId())
                .idType(dto.getIdType())
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(role)
                .specialty(defaultSpecialty)
                .active(true)
                .password(encodedPassword)
                .availableSchedule(new ArrayList<>())
                .build();
        
        Staff savedStaff = staffRepository.save(staff);
        log.info("Staff member created successfully: {}", savedStaff.getId());
        
        return mapToDto(savedStaff);
    }

    /**
     * Generates a default password for a user based on their ID.
     * The password follows the format: {userI}D@ECI 
     *
     * @param dto The user request data transfer object containing user information
     * @return A string representing the generated default password
     */
    private String generateDefaultPassword(UserRequestDTO dto) {
        return dto.getId() + "@ECI";
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponseDTO getStaffById(String id) {
        log.info("Retrieving staff member with ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create(STAFF, id));
        return mapToDto(staff);
    }

    @Override
    @Transactional
    public StaffResponseDTO updateStaff(String id, UserRequestDTO dto) {
        log.info("Updating staff member with ID: {}", id);
        
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create(STAFF, id));
        
        // Check if email is already used by another user
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw DuplicateResourceException.create("User", "email", dto.getEmail());
                    }
                });
        
        // Validate role
        Role role;
        try {
            role = Role.valueOf(dto.getRole().name());
        } catch (IllegalArgumentException e) {
            throw BadRequestException.invalidField("role", dto.getRole());
        }
        
        // Update staff fields
        staff.setFullName(dto.getFullName());
        staff.setIdType(dto.getIdType());
        staff.setPhone(dto.getPhone());
        staff.setEmail(dto.getEmail());
        staff.setRole(role);
        
        Staff savedStaff = staffRepository.save(staff);
        log.info("Staff member updated successfully: {}", savedStaff.getId());
        
        return mapToDto(savedStaff);
    }

    @Override
    @Transactional
    public void deleteStaff(String id) {
        log.info("Deleting staff member with ID: {}", id);
        
        if (!staffRepository.existsById(id)) {
            throw ResourceNotFoundException.create(STAFF, id);
        }
        
        staffRepository.deleteById(id);
        log.info("Staff member deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public ScheduleEntryDTO addStaffScheduleEntry(String staffId, ScheduleEntryDTO dto) {
        log.info("Adding schedule entry for staff member with ID: {}", staffId);
        
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> ResourceNotFoundException.create(STAFF, staffId));
        
        // Validate schedule entry
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BadRequestException("End time must be after start time");
        }
        
        // Check for overlapping schedule entries
        boolean hasOverlap = staff.getAvailableSchedule().stream()
                .anyMatch(entry -> 
                    (dto.getStartTime().isBefore(entry.getEndTime()) && 
                     dto.getEndTime().isAfter(entry.getStartTime()))
                );
        
        if (hasOverlap) {
            throw new BadRequestException("Schedule entry overlaps with an existing entry");
        }
        
        // Create and add schedule entry
        ExternalScheduleEntry entry = ExternalScheduleEntry.builder()
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
        
        staff.getAvailableSchedule().add(entry);
        Staff savedStaff = staffRepository.save(staff);
        
        // Find the saved entry (the one with ID)
        ExternalScheduleEntry savedEntry = savedStaff.getAvailableSchedule().stream()
                .filter(e -> e.getStartTime().equals(entry.getStartTime()) && 
                             e.getEndTime().equals(entry.getEndTime()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Saved entry not found"));
        
        log.info("Schedule entry added successfully: {}", savedEntry.getId());
        
        return ScheduleEntryDTO.builder()
                .id(savedEntry.getId())
                .startTime(savedEntry.getStartTime())
                .endTime(savedEntry.getEndTime())
                .build();
    }

    @Override
    @Transactional
    public void removeStaffScheduleEntry(String staffId, Long entryId) {
        log.info("Removing schedule entry {} for staff member with ID: {}", entryId, staffId);
        
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> ResourceNotFoundException.create(STAFF, staffId));
        
        boolean removed = staff.getAvailableSchedule().removeIf(entry -> entry.getId().equals(entryId));
        
        if (!removed) {
            throw ResourceNotFoundException.create("Schedule entry", entryId);
        }
        
        staffRepository.save(staff);
        log.info("Schedule entry removed successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDTO> getAvailableStaff(LocalDate date) {
        log.info("Retrieving staff members available on: {}", date);
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<Staff> availableStaff = staffRepository.findByAvailableScheduleStartTimeLessThanEqualAndAvailableScheduleEndTimeGreaterThanEqual(
                endOfDay, startOfDay);
        
        return availableStaff.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDTO> getStaffBySpecialty(String specialtyText) {
        log.info("Retrieving staff members with specialty: {}", specialtyText);
        
        List<Staff> staffList;
        
        try {
            // Try to convert to enum if it's an exact value
            Specialty specialty = Specialty.valueOf(specialtyText.toUpperCase());
            staffList = staffRepository.findBySpecialty(specialty);
        } catch (IllegalArgumentException e) {
            // If no match, try to convert to enum
            staffList = staffRepository.findBySpecialtyKeyword(specialtyText);
        }
        
        return staffList.stream()
                .map(this::mapToDto)
                .toList();
    }

    
    /**
     * Maps a Staff entity to a StaffResponseDTO.
     * 
     * @param staff the staff entity
     * @return the staff DTO
     */
    private StaffResponseDTO mapToDto(Staff staff) {
        List<ScheduleEntryDTO> scheduleEntries = null;
        
        if (staff.getAvailableSchedule() != null) {
            scheduleEntries = staff.getAvailableSchedule().stream()
                    .map(entry -> ScheduleEntryDTO.builder()
                            .id(entry.getId())
                            .startTime(entry.getStartTime())
                            .endTime(entry.getEndTime())
                            .build())
                    .toList();
        } else {
            scheduleEntries = new ArrayList<>();
        }
        
        return StaffResponseDTO.builder()
                .id(staff.getId())
                .idType(staff.getIdType())
                .fullName(staff.getFullName())
                .phone(staff.getPhone())
                .email(staff.getEmail())
                .role(staff.getRole())
                .specialty(staff.getSpecialty())
                .availableSchedule(scheduleEntries)
                .active(staff.isActive())
                .build();
    }
}
