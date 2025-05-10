package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.ExternalScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExternalScheduleEntryRepository extends JpaRepository<ExternalScheduleEntry, Long> {
    List<ExternalScheduleEntry> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
