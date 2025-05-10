package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.Student;
import edu.eci.cvds.users.model.enums.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByStudentCode(String studentCode);
    
    List<Student> findByProgram(Program program);
    
    List<Student> findBySemester(int semester);
    
    boolean existsByStudentCode(String studentCode);
    
    boolean existsByEmergencyContactId(Long emergencyContactId);
    
    @Query("SELECT s.program, COUNT(s) FROM Student s GROUP BY s.program")
    List<Object[]> countByProgramGroupByProgram();
    
    @Query("SELECT s.semester, COUNT(s) FROM Student s GROUP BY s.semester ORDER BY s.semester")
    List<Object[]> countBySemesterGroupBySemester();
}
