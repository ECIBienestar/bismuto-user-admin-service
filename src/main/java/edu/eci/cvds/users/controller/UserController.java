package edu.eci.cvds.users.controller;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import edu.eci.cvds.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users",
            description = "Retrieve a list of all users, including students and staff.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "List of users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID",
            description = "Retrieve a user by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create student", description = "Register a new student user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data")
    })
    @PostMapping("/students")
    public ResponseEntity<UserResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO dto) {
        UserResponseDTO created = userService.createStudent(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Create staff", description =
            "Register a new staff user (e.g., administrator).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid staff data")
    })
    @PostMapping("/staff")
    public ResponseEntity<UserResponseDTO> createStaff(
            @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Delete user by ID", description = "Delete a user based on their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}