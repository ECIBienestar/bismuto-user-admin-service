package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
