package edu.eci.cvds.users.service;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import edu.eci.cvds.users.dto.*;
import edu.eci.cvds.users.model.*;
import edu.eci.cvds.users.repository.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

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
        // 1) Search the emergency contact
        EmergencyContact ec = contactRepo.findById(dto.getEmergencyContactId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Emergency contact not found: " + dto.getEmergencyContactId()));


        // 2) Create student entity
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

        // 3) Map to response DTOs using ModelMapper
        UserResponseDTO response = new UserResponseDTO();
        return modelMapper.map(student, UserResponseDTO.class);
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
                null,             // specialty (only if apply)
                List.of()         // initially empty schedule
        );

        staffRepo.save(staff);

        // Map to response DTO using ModelMapper
        return modelMapper.map(staff, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        // Try to find the user as Student
        Optional<Student> optStudent = studentRepo.findById(id);
        if (optStudent.isPresent()) {
            Student s = optStudent.get();
            // Map to DTO using ModelMapper
            return modelMapper.map(s, UserResponseDTO.class);
        }

        // If not Student, try as Staff
        Staff staff = staffRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // Map to DTO using ModelMapper
        return modelMapper.map(staff, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> allUsers = studentRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toCollection(ArrayList::new));

        List<UserResponseDTO> staffUsers = staffRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        allUsers.addAll(staffUsers);
        return allUsers;
    }

    @Override
    public void deleteUserById(String id) {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
        } else if (staffRepo.existsById(id)) {
            staffRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
    }

    // Private helper method to map any User to DTO
    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setIdType(user.getIdType());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
