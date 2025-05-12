package edu.eci.cvds.users.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ExternalScheduleEntryTest {

    @Test
    void testBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(2);

        ExternalScheduleEntry entry = ExternalScheduleEntry.builder()
                .id(1L)
                .startTime(now)
                .endTime(later)
                .build();

        assertNotNull(entry);
        assertEquals(1L, entry.getId());
        assertEquals(now, entry.getStartTime());
        assertEquals(later, entry.getEndTime());
    }

    @Test
    void testNoArgsConstructor() {
        ExternalScheduleEntry entry = new ExternalScheduleEntry();

        assertNotNull(entry);
        assertNull(entry.getId());
        assertNull(entry.getStartTime());
        assertNull(entry.getEndTime());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(2);

        ExternalScheduleEntry entry = new ExternalScheduleEntry(1L, now, later);

        assertEquals(1L, entry.getId());
        assertEquals(now, entry.getStartTime());
        assertEquals(later, entry.getEndTime());
    }

    @Test
    void testIsValidTimeRangeWithValidRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(1);

        ExternalScheduleEntry entry = ExternalScheduleEntry.builder()
                .startTime(now)
                .endTime(later)
                .build();

        assertTrue(entry.isValidTimeRange());
    }

    @Test
    void testIsValidTimeRangeWithInvalidRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earlier = now.minusHours(1);

        ExternalScheduleEntry entry = ExternalScheduleEntry.builder()
                .startTime(now)
                .endTime(earlier)
                .build();

        assertFalse(entry.isValidTimeRange());
    }

    @Test
    void testIsValidTimeRangeWithNullTimes() {
        ExternalScheduleEntry entry = new ExternalScheduleEntry();
        assertTrue(entry.isValidTimeRange());
    }

    @Test
    void testOverlapsWithOverlappingEntries() {
        LocalDateTime base = LocalDateTime.now();
        ExternalScheduleEntry entry1 = ExternalScheduleEntry.builder()
                .startTime(base)
                .endTime(base.plusHours(2))
                .build();

        ExternalScheduleEntry entry2 = ExternalScheduleEntry.builder()
                .startTime(base.plusHours(1))
                .endTime(base.plusHours(3))
                .build();

        assertTrue(entry1.overlaps(entry2));
        assertTrue(entry2.overlaps(entry1));
    }

    @Test
    void testOverlapsWithNonOverlappingEntries() {
        LocalDateTime base = LocalDateTime.now();
        ExternalScheduleEntry entry1 = ExternalScheduleEntry.builder()
                .startTime(base)
                .endTime(base.plusHours(1))
                .build();

        ExternalScheduleEntry entry2 = ExternalScheduleEntry.builder()
                .startTime(base.plusHours(2))
                .endTime(base.plusHours(3))
                .build();

        assertFalse(entry1.overlaps(entry2));
        assertFalse(entry2.overlaps(entry1));
    }

    @Test
    void testOverlapsWithAdjacentEntries() {
        LocalDateTime base = LocalDateTime.now();
        ExternalScheduleEntry entry1 = ExternalScheduleEntry.builder()
                .startTime(base)
                .endTime(base.plusHours(1))
                .build();

        ExternalScheduleEntry entry2 = ExternalScheduleEntry.builder()
                .startTime(base.plusHours(1))
                .endTime(base.plusHours(2))
                .build();

        assertFalse(entry1.overlaps(entry2));
        assertFalse(entry2.overlaps(entry1));
    }

    @Test
    void testSetters() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(1);

        ExternalScheduleEntry entry = new ExternalScheduleEntry();
        entry.setId(1L);
        entry.setStartTime(now);
        entry.setEndTime(later);

        assertEquals(1L, entry.getId());
        assertEquals(now, entry.getStartTime());
        assertEquals(later, entry.getEndTime());
    }

    @Test
    void testIsValidTimeRangeWithNullStartTime() {
        LocalDateTime endTime = LocalDateTime.now();

        ExternalScheduleEntry entry = ExternalScheduleEntry.builder()
                .startTime(null)
                .endTime(endTime)
                .build();

        assertTrue(entry.isValidTimeRange());
    }

    @Test
    void testIsValidTimeRangeWithNullEndTime() {
        LocalDateTime startTime = LocalDateTime.now();

        ExternalScheduleEntry entry = ExternalScheduleEntry.builder()
                .startTime(startTime)
                .endTime(null)
                .build();

        assertTrue(entry.isValidTimeRange());
    }
}