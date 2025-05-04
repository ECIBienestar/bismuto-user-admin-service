package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import jakarta.validation.Valid;
import edu.eci.cvds.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users (students + staff)
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get users by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Create a student
    @PostMapping("/students")
    public ResponseEntity<UserResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO dto) {
        UserResponseDTO created = userService.createStudent(dto);
        return ResponseEntity.ok(created);
    }

    // Create a staff (administrator)
    @PostMapping("/staff")
    public ResponseEntity<UserResponseDTO> createStaff(
            @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}