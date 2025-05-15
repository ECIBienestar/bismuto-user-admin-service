package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.ScheduleEntryDTO;
import edu.eci.cvds.users.dto.StaffResponseDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for staff-specific operations.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public interface StaffService {

    /**
     * Creates a new staff member.
     * 
     * @param dto the staff data
     * @return the created staff member
     */
    StaffResponseDTO createStaff(UserRequestDTO dto);

    /**
     * Retrieves a staff member by their ID.
     * 
     * @param id the staff ID
     * @return the staff member with the given ID
     */
    StaffResponseDTO getStaffById(String id);

    /**
     * Updates a staff member's information.
     * 
     * @param id  the staff ID
     * @param dto the updated staff data
     * @return the updated staff member
     */
    StaffResponseDTO updateStaff(String id, UserRequestDTO dto);

    /**
     * Deletes a staff member by their ID.
     * 
     * @param id the staff ID
     */
    void deleteStaff(String id);

    /**
     * Adds a schedule entry to a staff member's available schedule.
     * 
     * @param staffId the staff ID
     * @param dto     the schedule entry data
     * @return the created schedule entry
     */
    ScheduleEntryDTO addStaffScheduleEntry(String staffId, ScheduleEntryDTO dto);

    /**
     * Removes a schedule entry from a staff member's available schedule.
     * 
     * @param staffId the staff ID
     * @param entryId the schedule entry ID
     */
    void removeStaffScheduleEntry(String staffId, Long entryId);

    /**
     * Retrieves all staff members available on a specific date.
     * 
     * @param date the date to check availability
     * @return list of available staff members
     */
    List<StaffResponseDTO> getAvailableStaff(LocalDate date);

    /**
     * Retrieves all staff members with a specific specialty.
     * 
     * @param specialty the specialty to filter by
     * @return list of staff members with the specified specialty
     */
    List<StaffResponseDTO> getStaffBySpecialty(String specialtyText);
}
