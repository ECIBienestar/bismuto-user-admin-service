package edu.eci.cvds.users.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import edu.eci.cvds.users.dto.*;
import edu.eci.cvds.users.model.*;
import edu.eci.cvds.users.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmergencyContactRepository contactRepo;

    @Override
    public UserResponseDTO createStudent(StudentRequestDTO dto) {
        // 1) Busca el EmergencyContact
        EmergencyContact ec = contactRepo.findById(dto.getEmergencyContactId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Emergency contact not found: " + dto.getEmergencyContactId()));


        // 2) Crea la entidad Student
        Student student = new Student(
                dto.getId(),
                dto.getIdType(),
                dto.getFullName(),
                dto.getPhone(),
                dto.getEmail(),
                Role.valueOf(dto.getRole()),
                dto.getStudentCode(),
                dto.getProgram(),
                dto.getBirthDate(),
                ec,
                dto.getAddress()
        );
        studentRepo.save(student);

        // 3) Mapea y devuelve con setters
        UserResponseDTO response = new UserResponseDTO();
        response.setId        (student.getId());
        response.setIdType    (student.getIdType());
        response.setFullName  (student.getFullName());
        response.setPhone     (student.getPhone());
        response.setEmail     (student.getEmail());
        response.setRole      (student.getRole().name());
        return response;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        Staff staff = new Staff(
                dto.getId(),
                dto.getIdType(),
                dto.getFullName(),
                dto.getPhone(),
                dto.getEmail(),
                Role.valueOf(dto.getRole()),
                null,             // specialty (solo si aplica)
                List.of()         // schedule vacío inicialmente
        );

        // Mapear a DTO de respuesta
        UserResponseDTO response = new UserResponseDTO();
        response.setId        (staff.getId());
        response.setIdType    (staff.getIdType());
        response.setFullName  (staff.getFullName());
        response.setPhone     (staff.getPhone());
        response.setEmail     (staff.getEmail());
        response.setRole      (staff.getRole().name());
        return response;
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        // Intentar encontrar al user como Student
        Optional<Student> optStudent = studentRepo.findById(id);
        if (optStudent.isPresent()) {
            Student s = optStudent.get();
            UserResponseDTO response = new UserResponseDTO();
            response.setId        (s.getId());
            response.setIdType    (s.getIdType());
            response.setFullName  (s.getFullName());
            response.setPhone     (s.getPhone());
            response.setEmail     (s.getEmail());
            response.setRole      (s.getRole().name());
            return response;
        }

        // Si no es Student, intentar como Staff
        Staff staff = staffRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        UserResponseDTO response = new UserResponseDTO();
        response.setId        (staff.getId());
        response.setIdType    (staff.getIdType());
        response.setFullName  (staff.getFullName());
        response.setPhone     (staff.getPhone());
        response.setEmail     (staff.getEmail());
        response.setRole      (staff.getRole().name());
        return response;
    }
}
