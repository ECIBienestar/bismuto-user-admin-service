package edu.eci.cvds.users.service.impl;

import edu.eci.cvds.users.dto.EmergencyContactDTO;
import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.StudentResponseDTO;
import edu.eci.cvds.users.exception.BadRequestException;
import edu.eci.cvds.users.exception.DuplicateResourceException;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.EmergencyContact;
import edu.eci.cvds.users.model.Student;
import edu.eci.cvds.users.model.enums.Program;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.repository.EmergencyContactRepository;
import edu.eci.cvds.users.repository.StudentRepository;
import edu.eci.cvds.users.repository.UserRepository;
import edu.eci.cvds.users.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the StudentService interface.
 * 
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT = "Student";
    private final StudentRepository studentRepository;
    private final EmergencyContactRepository contactRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        log.info("Creating new student with ID: {}", dto.getId());
        
        // Check if user with same ID or email already exists
        if (userRepository.existsById(dto.getId())) {
            throw DuplicateResourceException.create("User", "id", dto.getId());
        }
        
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw DuplicateResourceException.create("User", "email", dto.getEmail());
        }
        
        // Check if student code is already used
        if (studentRepository.findByStudentCode(dto.getStudentCode()).isPresent()) {
            throw DuplicateResourceException.create(STUDENT, "studentCode", dto.getStudentCode());
        }
        
        // Get emergency contact
        EmergencyContact contact = contactRepository.findById(dto.getEmergencyContactId())
                .orElseThrow(() -> ResourceNotFoundException.create("EmergencyContact", dto.getEmergencyContactId()));
        
        // Create student
        Student student = Student.builder()
                .id(dto.getId())
                .idType(dto.getIdType())
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .role(Role.STUDENT)
                .studentCode(dto.getStudentCode())
                .program(dto.getProgram())
                .semester(dto.getSemester())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .emergencyContact(contact)
                .build();
        
        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully: {}", savedStudent.getId());
        
        return mapToDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(String id) {
        log.info("Retrieving student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create(STUDENT, id));
        return mapToDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(String id, StudentRequestDTO dto) {
        log.info("Updating student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create(STUDENT, id));
        
        // Check if student code is already used by another student
        studentRepository.findByStudentCode(dto.getStudentCode())
                .ifPresent(existingStudent -> {
                    if (!existingStudent.getId().equals(id)) {
                        throw DuplicateResourceException.create(STUDENT, "studentCode", dto.getStudentCode());
                    }
                });
        
        // Check if email is already used by another user
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw DuplicateResourceException.create("User", "email", dto.getEmail());
                    }
                });
        
        // Get emergency contact
        EmergencyContact contact = contactRepository.findById(dto.getEmergencyContactId())
                .orElseThrow(() -> ResourceNotFoundException.create("EmergencyContact", dto.getEmergencyContactId()));
        
        // Update student fields
        student.setFullName(dto.getFullName());
        student.setIdType(dto.getIdType());
        student.setPhone(dto.getPhone());
        student.setEmail(dto.getEmail());
        student.setStudentCode(dto.getStudentCode());
        student.setProgram(dto.getProgram());
        student.setSemester(dto.getSemester());
        student.setBirthDate(dto.getBirthDate());
        student.setAddress(dto.getAddress());
        student.setEmergencyContact(contact);
        
        Student savedStudent = studentRepository.save(student);
        log.info("Student updated successfully: {}", savedStudent.getId());
        
        return mapToDto(savedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(String id) {
        log.info("Deleting student with ID: {}", id);
        
        if (!studentRepository.existsById(id)) {
            throw ResourceNotFoundException.create(STUDENT, id);
        }
        
        studentRepository.deleteById(id);
        log.info("Student deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByProgram(Program program) {
        log.info("Retrieving students in program: {}", program);
        return studentRepository.findByProgram(program).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsBySemester(int semester) {
        log.info("Retrieving students in semester: {}", semester);
        
        if (semester < 1 || semester > 12) {
            throw BadRequestException.invalidField("semester", semester);
        }
        
        return studentRepository.findBySemester(semester).stream()
                .map(this::mapToDto)
                .toList();
    }
    
    /**
     * Maps a Student entity to a StudentResponseDTO.
     * 
     * @param student the student entity
     * @return the student DTO
     */
    private StudentResponseDTO mapToDto(Student student) {
        EmergencyContactDTO contactDto = null;
        
        if (student.getEmergencyContact() != null) {
            EmergencyContact contact = student.getEmergencyContact();
            contactDto = EmergencyContactDTO.builder()
                    .id(contact.getId())
                    .fullName(contact.getFullName())
                    .phone(contact.getPhone())
                    .idType(contact.getIdType())
                    .idNumber(contact.getIdNumber())
                    .relationship(contact.getRelationship())
                    .build();
        }
        
        return StudentResponseDTO.builder()
                .id(student.getId())
                .idType(student.getIdType())
                .fullName(student.getFullName())
                .phone(student.getPhone())
                .email(student.getEmail())
                .role(student.getRole())
                .studentCode(student.getStudentCode())
                .program(student.getProgram())
                .semester(student.getSemester())
                .birthDate(student.getBirthDate())
                .address(student.getAddress())
                .emergencyContact(contactDto)
                .build();
    }
}
