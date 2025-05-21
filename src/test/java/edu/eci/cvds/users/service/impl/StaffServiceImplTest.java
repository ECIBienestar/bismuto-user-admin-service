package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.ScheduleEntryDTO;
import edu.eci.cvds.users.dto.StaffRequestDTO;
import edu.eci.cvds.users.dto.StaffResponseDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.DuplicateResourceException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.ExternalScheduleEntry;
import edu.eci.cvds.users.model.Staff;
import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.model.enums.Specialty;
import edu.eci.cvds.users.repository.StaffRepository;
import edu.eci.cvds.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceImplTest {

    private StaffRepository staffRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private StaffServiceImpl staffService;

    @BeforeEach
    void setUp() {
        staffRepository = mock(StaffRepository.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        staffService = new StaffServiceImpl(staffRepository, userRepository, passwordEncoder);
    }

    @Test
    void testCreateStaffSuccess() {
        StaffRequestDTO dto = StaffRequestDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("Dr. House")
                .email("house@eci.edu.co")
                .phone("123456")
                .role(Role.MEDICAL_STAFF)
                .build();

        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(staffRepository.save(any(Staff.class))).thenAnswer(i -> i.getArguments()[0]);

        StaffResponseDTO response = staffService.createStaff(dto);

        assertEquals("123", response.getId());
        assertEquals(Role.MEDICAL_STAFF, response.getRole());
        assertEquals(Specialty.GENERAL_MEDICINE, response.getSpecialty());
        verify(staffRepository).save(any());
    }

    @Test
    void testCreateStaffDuplicateEmailThrowsException() {
        StaffRequestDTO dto = StaffRequestDTO.builder()
                .id("123")
                .email("dup@eci.edu.co")
                .role(Role.TRAINER)
                .build();

        Staff existingStaff = Staff.builder()
                .id("456")
                .email("dup@eci.edu.co")
                .fullName("Carlos López")
                .idType(IdType.CC)
                .phone("3001234567")
                .role(Role.TRAINER)
                .specialty(Specialty.FITNESS_COACH)
                .availableSchedule(new ArrayList<>())
                .active(true)
                .password("encryptedPassword")
                .build();

        when(userRepository.existsById("123")).thenReturn(false);
        when(userRepository.findByEmail("dup@eci.edu.co")).thenReturn(Optional.of(existingStaff));

        assertThrows(DuplicateResourceException.class, () -> staffService.createStaff(dto));
    }

    @Test
    void testGetStaffById_found() {
        Staff staff = Staff.builder().id("123").fullName("Name").build();
        when(staffRepository.findById("123")).thenReturn(Optional.of(staff));

        StaffResponseDTO dto = staffService.getStaffById("123");

        assertEquals("123", dto.getId());
    }

    @Test
    void testGetStaffById_notFound() {
        when(staffRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.getStaffById("999"));
    }

    @Test
    void testAddStaffScheduleEntry_success() {
        String staffId = "123";
        LocalDateTime start = LocalDateTime.of(2025, 5, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 10, 11, 0);
        ExternalScheduleEntry existingEntry = ExternalScheduleEntry.builder()
                .id(1L)
                .startTime(LocalDateTime.of(2025, 5, 10, 8, 0))
                .endTime(LocalDateTime.of(2025, 5, 10, 9, 0))
                .build();

        Staff staff = Staff.builder()
                .id(staffId)
                .availableSchedule(new ArrayList<>(List.of(existingEntry)))
                .build();

        ScheduleEntryDTO dto = ScheduleEntryDTO.builder().startTime(start).endTime(end).build();

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        when(staffRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleEntryDTO saved = staffService.addStaffScheduleEntry(staffId, dto);

        assertEquals(start, saved.getStartTime());
        assertEquals(end, saved.getEndTime());
    }

    @Test
    void testAddStaffScheduleEntry_overlap_throwsException() {
        String staffId = "123";
        LocalDateTime start = LocalDateTime.of(2025, 5, 10, 9, 30);
        LocalDateTime end = LocalDateTime.of(2025, 5, 10, 10, 30);
        ExternalScheduleEntry conflictingEntry = ExternalScheduleEntry.builder()
                .startTime(LocalDateTime.of(2025, 5, 10, 9, 0))
                .endTime(LocalDateTime.of(2025, 5, 10, 10, 0))
                .build();

        Staff staff = Staff.builder()
                .id(staffId)
                .availableSchedule(new ArrayList<>(List.of(conflictingEntry)))
                .build();

        ScheduleEntryDTO dto = ScheduleEntryDTO.builder().startTime(start).endTime(end).build();

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));

        assertThrows(BadRequestException.class, () -> staffService.addStaffScheduleEntry(staffId, dto));
    }

    @Test
    void testDeleteStaff_success() {
        when(staffRepository.existsById("123")).thenReturn(true);
        staffService.deleteStaff("123");
        verify(staffRepository).deleteById("123");
    }

    @Test
    void testDeleteStaff_notFound() {
        when(staffRepository.existsById("999")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> staffService.deleteStaff("999"));
    }

    @Test
    void testGetAvailableStaff() {
        Staff staff1 = Staff.builder().id("1").build();
        Staff staff2 = Staff.builder().id("2").build();
        LocalDate date = LocalDate.of(2025, 5, 10);

        when(staffRepository.findByAvailableScheduleStartTimeLessThanEqualAndAvailableScheduleEndTimeGreaterThanEqual(
                any(), any())).thenReturn(List.of(staff1, staff2));

        List<StaffResponseDTO> result = staffService.getAvailableStaff(date);
        assertEquals(2, result.size());
    }

    @Test
    void testAddStaffScheduleEntryThrowsExceptionWhenStartTimeAfterEndTime() {
        ScheduleEntryDTO dto = ScheduleEntryDTO.builder()
                .startTime(LocalDateTime.of(2025, 5, 11, 15, 0))
                .endTime(LocalDateTime.of(2025, 5, 11, 14, 0))
                .build();

        Staff staff = new Staff();
        when(staffRepository.findById("staff123")).thenReturn(Optional.of(staff));

        assertThrows(BadRequestException.class, () -> staffService.addStaffScheduleEntry("staff123", dto));
    }

    @Test
    void testGetStaffBySpecialtyWithInvalidEnumFallsBackToKeywordSearch() {
        List<Staff> mockStaffList = List.of(new Staff());
        when(staffRepository.findBySpecialtyKeyword("notanenum")).thenReturn(mockStaffList);

        List<StaffResponseDTO> result = staffService.getStaffBySpecialty("notanenum");

        assertNotNull(result);
        verify(staffRepository).findBySpecialtyKeyword("notanenum");
    }

    @Test
    void testMapToDto() {
        Staff staff = new Staff();
        staff.setId("staff123");
        staff.setIdType(IdType.CC);
        staff.setFullName("Juan Pérez");
        staff.setPhone("3001234567");
        staff.setEmail("juan.perez@eci.edu.co");
        staff.setRole(Role.TRAINER);
        staff.setSpecialty(Specialty.FITNESS_COACH);
        staff.setActive(true);

        ExternalScheduleEntry scheduleEntry1 = new ExternalScheduleEntry();
        scheduleEntry1.setId(1L);
        scheduleEntry1.setStartTime(LocalDateTime.of(2025, 5, 11, 9, 0));
        scheduleEntry1.setEndTime(LocalDateTime.of(2025, 5, 11, 12, 0));

        ExternalScheduleEntry scheduleEntry2 = new ExternalScheduleEntry();
        scheduleEntry1.setId(2L);
        scheduleEntry2.setStartTime(LocalDateTime.of(2025, 5, 11, 14, 0));
        scheduleEntry2.setEndTime(LocalDateTime.of(2025, 5, 11, 16, 0));

        staff.setAvailableSchedule(Arrays.asList(scheduleEntry1, scheduleEntry2));

        StaffResponseDTO dto = staffService.getStaffResponseDTO(staff);

        assertEquals("staff123", dto.getId());
        assertEquals(IdType.CC, dto.getIdType());
        assertEquals("Juan Pérez", dto.getFullName());
        assertEquals("3001234567", dto.getPhone());
        assertEquals("juan.perez@eci.edu.co", dto.getEmail());
        assertEquals(Role.TRAINER, dto.getRole());
        assertEquals(Specialty.FITNESS_COACH, dto.getSpecialty());
        assertTrue(dto.isActive());

        List<ScheduleEntryDTO> scheduleEntries = dto.getAvailableSchedule();
        assertNotNull(scheduleEntries);
        assertEquals(2, scheduleEntries.size());

        ScheduleEntryDTO entry1 = scheduleEntries.get(0);
        assertEquals(2, entry1.getId());
        assertEquals(LocalDateTime.of(2025, 5, 11, 9, 0), entry1.getStartTime());
        assertEquals(LocalDateTime.of(2025, 5, 11, 12, 0), entry1.getEndTime());

        ScheduleEntryDTO entry2 = scheduleEntries.get(1);
        assertEquals(null, entry2.getId());
        assertEquals(LocalDateTime.of(2025, 5, 11, 14, 0), entry2.getStartTime());
        assertEquals(LocalDateTime.of(2025, 5, 11, 16, 0), entry2.getEndTime());
    }

    @Test
    void shouldRemoveScheduleEntryFromStaff() {
        String staffId = "123";
        Long entryId = 1L;

        ExternalScheduleEntry entry = new ExternalScheduleEntry();
        entry.setId(entryId);

        Staff staff = new Staff();
        staff.setId(staffId);
        staff.setAvailableSchedule(new ArrayList<>(List.of(entry)));

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));

        staffService.removeStaffScheduleEntry(staffId, entryId);

        assertTrue(staff.getAvailableSchedule().isEmpty());
        verify(staffRepository).save(staff);
    }

    @Test
    void shouldUpdateStaffSuccessfully() {
        String staffId = "staff123";

        StaffRequestDTO dto = StaffRequestDTO.builder()
                .fullName("Nuevo Nombre")
                .idType(IdType.CC)
                .phone("3216549870")
                .email("nuevo@email.com")
                .role(Role.MEDICAL_STAFF)
                .build();

        Staff existingStaff = new Staff();
        existingStaff.setId(staffId);
        existingStaff.setEmail("viejo@email.com");

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(existingStaff));
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(staffRepository.save(any(Staff.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StaffResponseDTO result = staffService.updateStaff(staffId, dto);

        assertEquals(dto.getFullName(), result.getFullName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getPhone(), result.getPhone());
        assertEquals(dto.getIdType(), result.getIdType());
        assertEquals(Role.MEDICAL_STAFF, result.getRole());

        verify(staffRepository).save(any(Staff.class));
    }

    @Test
    void mapToDtoWithNullScheduleReturnsEmptyList() {
        Staff staff = new Staff();

        staff.setAvailableSchedule(null);

        StaffResponseDTO result = staffService.mapToDto(staff);

        assertNotNull(result.getAvailableSchedule());
        assertTrue(result.getAvailableSchedule().isEmpty());
    }

    @Test
    void getStaffBySpecialtyValidEnumReturnsStaffList() {
        Staff staff = new Staff();

        when(staffRepository.findBySpecialty(Specialty.GENERAL_MEDICINE))
                .thenReturn(List.of(staff));

        List<StaffResponseDTO> result = staffService.getStaffBySpecialty("GENERAL_MEDICINE");

        assertEquals(1, result.size());
        verify(staffRepository, times(1)).findBySpecialty(Specialty.GENERAL_MEDICINE);
        verify(staffRepository, never()).findBySpecialtyKeyword(any());
    }

    @Test
    void removeStaffScheduleEntryEntryNotFoundThrowsResourceNotFoundException() {
        Staff staff = new Staff();

        when(staffRepository.findById("123")).thenReturn(Optional.of(staff));

        assertThrows(ResourceNotFoundException.class, () -> {
            staffService.removeStaffScheduleEntry("123", 1L);
        });

        verify(staffRepository, never()).save(any());
    }

    @Test
    void createStaffWithExistingIdThrowsDuplicateResourceException() {
        StaffRequestDTO staffRequestDTO = StaffRequestDTO.builder()
                .id("123")
                .email("asad")
                .build();

        when(userRepository.existsById("123")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            staffService.createStaff(staffRequestDTO);
        });

        verify(userRepository, times(1)).existsById("123");
        verify(userRepository, never()).findByEmail(any());
    }
}
