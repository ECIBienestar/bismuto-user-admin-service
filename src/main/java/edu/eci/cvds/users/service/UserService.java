package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;

public interface UserService {
    // 1) Add student
    UserResponseDTO createStudent(StudentRequestDTO dto);

    // 2) Add administrator (or other staff rol)
    UserResponseDTO createUser(UserRequestDTO dto);

    // 3) Get user by ID
    UserResponseDTO getUserById(String id);
}
