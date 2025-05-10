package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.User;
import edu.eci.cvds.users.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(Role role);
    
    boolean existsByEmail(String email);
    
    long countByActiveTrue();
    
    long countByActiveFalse();
    
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countByRoleGroupByRole();
}
