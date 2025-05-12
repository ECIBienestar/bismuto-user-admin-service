package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.enums.Specialty;
import edu.eci.cvds.users.model.enums.IdType;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StaffResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        StaffResponseDTO staff = new StaffResponseDTO();

        assertNull(staff.getSpecialty());
        assertNotNull(staff.getAvailableSchedule());
        assertTrue(staff.getAvailableSchedule().isEmpty());
    }

    @Test
    void testBuilder() {
        List<ScheduleEntryDTO> schedule = new ArrayList<>();
        ScheduleEntryDTO entry = new ScheduleEntryDTO();
        entry.setStartTime(LocalDateTime.now());
        entry.setEndTime(LocalDateTime.now().plusHours(1));
        schedule.add(entry);

        // Usa los valores exactos de tus enums
        StaffResponseDTO staff = StaffResponseDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("Dr. Smith")
                .email("doctor@example.com")
                .phone("555-1234")
                .specialty(Specialty.DENTISTRY)
                .availableSchedule(schedule)
                .build();

        assertEquals("123", staff.getId());
        assertEquals(IdType.CC, staff.getIdType());
        assertEquals("Dr. Smith", staff.getFullName());
        assertEquals("doctor@example.com", staff.getEmail());
        assertEquals("555-1234", staff.getPhone());
        assertEquals(Specialty.DENTISTRY, staff.getSpecialty());
        assertEquals(1, staff.getAvailableSchedule().size());
    }

    @Test
    void testAddScheduleEntry() {
        StaffResponseDTO staff = new StaffResponseDTO();
        ScheduleEntryDTO entry = new ScheduleEntryDTO();
        entry.setStartTime(LocalDateTime.of(2025, 5, 15, 9, 0));
        entry.setEndTime(LocalDateTime.of(2025, 5, 15, 10, 0));

        staff.addScheduleEntry(entry);
        assertEquals(1, staff.getAvailableSchedule().size());

        staff.setAvailableSchedule(null);
        staff.addScheduleEntry(entry);
        assertEquals(1, staff.getAvailableSchedule().size());
    }

    @Test
    void testEqualsAndHashCode() {
        StaffResponseDTO staff1 = StaffResponseDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("Dr. Smith")
                .build();

        StaffResponseDTO staff2 = StaffResponseDTO.builder()
                .id("123")
                .idType(IdType.CC)
                .fullName("Dr. Smith")
                .build();

        StaffResponseDTO staff3 = StaffResponseDTO.builder()
                .id("456")
                .idType(IdType.TI)
                .fullName("Dr. Smith")
                .build();

        assertEquals(staff1, staff2);
        assertNotEquals(staff1, staff3);
        assertEquals(staff1.hashCode(), staff2.hashCode());
        assertNotEquals(staff1.hashCode(), staff3.hashCode());
    }
}