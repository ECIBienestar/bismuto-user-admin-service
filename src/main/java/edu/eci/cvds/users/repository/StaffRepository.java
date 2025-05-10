package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.Staff;
import edu.eci.cvds.users.model.enums.Specialty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, String> {
    List<Staff> findBySpecialty(Specialty specialty);

    @Query("SELECT s FROM Staff s WHERE CAST(s.specialty AS string) LIKE %:keyword%")
    List<Staff> findBySpecialtyKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT s FROM Staff s JOIN s.availableSchedule a WHERE a.startTime <= :endTime AND a.endTime >= :startTime")
    List<Staff> findByAvailableScheduleStartTimeLessThanEqualAndAvailableScheduleEndTimeGreaterThanEqual(
            @Param("endTime") LocalDateTime endTime, 
            @Param("startTime") LocalDateTime startTime);
}
