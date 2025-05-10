package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.StudentResponseDTO;
import edu.eci.cvds.users.model.enums.Program;

import java.util.List;

/**
 * Service interface for student-specific operations.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
public interface StudentService {
    
    /**
     * Creates a new student.
     * 
     * @param dto the student data
     * @return the created student
     */
    StudentResponseDTO createStudent(StudentRequestDTO dto);
    
    /**
     * Retrieves a student by their ID.
     * 
     * @param id the student ID
     * @return the student with the given ID
     */
    StudentResponseDTO getStudentById(String id);
    
    /**
     * Updates a student's information.
     * 
     * @param id the student ID
     * @param dto the updated student data
     * @return the updated student
     */
    StudentResponseDTO updateStudent(String id, StudentRequestDTO dto);
    
    /**
     * Deletes a student by their ID.
     * 
     * @param id the student ID
     */
    void deleteStudent(String id);
    
    /**
     * Retrieves all students in a specific academic program.
     * 
     * @param program the academic program
     * @return list of students in the specified program
     */
    List<StudentResponseDTO> getStudentsByProgram(Program program);
    
    /**
     * Retrieves all students in a specific semester.
     * 
     * @param semester the semester number
     * @return list of students in the specified semester
     */
    List<StudentResponseDTO> getStudentsBySemester(int semester);
}
