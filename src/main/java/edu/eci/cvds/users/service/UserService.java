package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    // 1) Add student
    UserResponseDTO createStudent(StudentRequestDTO dto);

    // 2) Add administrator (or other staff rol)
    UserResponseDTO createUser(UserRequestDTO dto);

    // 3) Get user by ID
    UserResponseDTO getUserById(String id);

    // 4) Get all users (students and staff)
    List<UserResponseDTO> getAllUsers();

    // 5) Delete user by ID
    void deleteUserById(String id);
}
