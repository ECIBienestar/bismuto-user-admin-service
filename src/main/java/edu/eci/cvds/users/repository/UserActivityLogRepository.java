package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, String> {
    List<UserActivityLog> findByUserIdOrderByTimestampDesc(String userId);
    
    List<UserActivityLog> findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime startDate, LocalDateTime endDate);
    
    List<UserActivityLog> findByUserIdAndTimestampBetweenOrderByTimestampDesc(
            String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<UserActivityLog> findByTimestampAfterOrderByTimestampDesc(LocalDateTime startDate);
    
    List<UserActivityLog> findByUserIdAndTimestampAfterOrderByTimestampDesc(
            String userId, LocalDateTime startDate);
    
    List<UserActivityLog> findByTimestampBeforeOrderByTimestampDesc(LocalDateTime endDate);
    
    List<UserActivityLog> findByUserIdAndTimestampBeforeOrderByTimestampDesc(
            String userId, LocalDateTime endDate);
    
    void deleteByUserId(String userId);
}
