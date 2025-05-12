package edu.eci.cvds.users.model;

import edu.eci.cvds.users.model.enums.Specialty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaffTest {

    private Staff staff;
    private ExternalScheduleEntry entry1;
    private ExternalScheduleEntry entry2;

    @BeforeEach
    void setUp() {
        staff = Staff.builder()
                .id("staff123")
                .fullName("John Doe")
                .email("john.doe@example.com")
                .specialty(Specialty.GENERAL_MEDICINE)
                .build();

        entry1 = ExternalScheduleEntry.builder()
                .id(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        entry2 = ExternalScheduleEntry.builder()
                .id(2L)
                .startTime(LocalDateTime.now().plusHours(3))
                .endTime(LocalDateTime.now().plusHours(5))
                .build();
    }

    @Test
    void testStaffBuilder() {
        assertNotNull(staff);
        assertEquals("staff123", staff.getId());
        assertEquals("John Doe", staff.getFullName());
        assertEquals("john.doe@example.com", staff.getEmail());
        assertEquals(Specialty.GENERAL_MEDICINE, staff.getSpecialty());
        assertNotNull(staff.getAvailableSchedule());
        assertTrue(staff.getAvailableSchedule().isEmpty());
    }

    @Test
    void testAddScheduleEntry() {
        boolean result = staff.addScheduleEntry(entry1);

        assertTrue(result);
        assertEquals(1, staff.getAvailableSchedule().size());
        assertTrue(staff.getAvailableSchedule().contains(entry1));
    }

    @Test
    void testAddMultipleScheduleEntries() {
        staff.addScheduleEntry(entry1);
        staff.addScheduleEntry(entry2);

        assertEquals(2, staff.getAvailableSchedule().size());
        assertTrue(staff.getAvailableSchedule().containsAll(List.of(entry1, entry2)));
    }

    @Test
    void testRemoveScheduleEntry() {
        staff.addScheduleEntry(entry1);
        staff.addScheduleEntry(entry2);

        boolean result = staff.removeScheduleEntry(entry1);

        assertTrue(result);
        assertEquals(1, staff.getAvailableSchedule().size());
        assertFalse(staff.getAvailableSchedule().contains(entry1));
        assertTrue(staff.getAvailableSchedule().contains(entry2));
    }

    @Test
    void testRemoveNonExistentScheduleEntry() {
        staff.addScheduleEntry(entry1);

        boolean result = staff.removeScheduleEntry(entry2);

        assertFalse(result);
        assertEquals(1, staff.getAvailableSchedule().size());
    }

    @Test
    void testSpecialtySetter() {
        staff.setSpecialty(Specialty.FITNESS_COACH);

        assertEquals(Specialty.FITNESS_COACH, staff.getSpecialty());
    }

    @Test
    void testAvailableScheduleSetter() {
        List<ExternalScheduleEntry> newSchedule = List.of(entry1, entry2);

        staff.setAvailableSchedule(newSchedule);

        assertEquals(2, staff.getAvailableSchedule().size());
        assertTrue(staff.getAvailableSchedule().containsAll(newSchedule));
    }

    @Test
    void testNoArgsConstructor() {
        Staff emptyStaff = new Staff();

        assertNotNull(emptyStaff);
        assertNull(emptyStaff.getId());
        assertNull(emptyStaff.getFullName());
        assertNull(emptyStaff.getSpecialty());
        assertNotNull(emptyStaff.getAvailableSchedule());
        assertTrue(emptyStaff.getAvailableSchedule().isEmpty());
    }
}