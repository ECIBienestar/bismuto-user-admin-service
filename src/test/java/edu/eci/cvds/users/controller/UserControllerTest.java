package edu.eci.cvds.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.cvds.users.dto.*;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    // To serialize/deserialize JSON
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /users → 200 OK + JSON array")
    void getAllUsersWorks() throws Exception {
        var u1 = new UserResponseDTO("A","CC","Alice",123,"a@e","TRAINER");
        var u2 = new UserResponseDTO("B","CC","Bob",456,"b@e","ADMINISTRATOR");
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(u1,u2));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName", is("Alice")))
                .andExpect(jsonPath("$[1].role", is("ADMINISTRATOR")));
    }

    @Test
    @DisplayName("GET /users/{id} exists → 200 + JSON")
    void getUserByIdFound() throws Exception {
        var u = new UserResponseDTO("X","CC","Xavier",789,"x@e","TRAINER");
        Mockito.when(userService.getUserById("X")).thenReturn(u);

        mvc.perform(get("/users/X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("x@e")));
    }

    @Test
    @DisplayName("GET /users/{id} missing → 404")
    void getUserByIdNotFound() throws Exception {
        Mockito.when(userService.getUserById("ZZ"))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mvc.perform(get("/users/ZZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /users/students valid → 200 + created student")
    void createStudentValid() throws Exception {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setId("S1"); dto.setIdType("CC"); dto.setFullName("Stu"); dto.setPhone(111);
        dto.setEmail("stu@e"); dto.setRole("WELLNESS_STAFF");
        dto.setStudentCode("SC1"); dto.setProgram("Eng");
        dto.setBirthDate(LocalDate.of(2001,1,1)); dto.setAddress("Somewhere");
        dto.setEmergencyContactId(45661L);

        var resp = new UserResponseDTO("S1","CC","Stu",111,"stu@e","WELLNESS_STAFF");
        Mockito.when(userService.createStudent(any(StudentRequestDTO.class))).thenReturn(resp);

        mvc.perform(post("/users/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("S1")))
                .andExpect(jsonPath("$.role", is("WELLNESS_STAFF")));
    }

    @Test
    @DisplayName("POST /users/students invalid → 400")
    void createStudentInvalid() throws Exception {
        // falta el fullName → violación de @NotBlank
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setId("S1");
        // dto.setFullName(null);

        mvc.perform(post("/users/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity()); // 422 de tu Handler
    }

    @Test
    @DisplayName("DELETE /users/{id} exists → 204")
    void deleteUserWorks() throws Exception {
        mvc.perform(delete("/users/X"))
                .andExpect(status().isNoContent());
        Mockito.verify(userService).deleteUserById("X");
    }

    @Test
    @DisplayName("DELETE /users/{id} missing → 404")
    void deleteUserNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("nope"))
                .when(userService).deleteUserById("ZZ");

        mvc.perform(delete("/users/ZZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /users/staff valid → 200 + created staff")
    void createStaffValid() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setId("A1");
        dto.setIdType("CC");
        dto.setFullName("Admin User");
        dto.setPhone(123456789);
        dto.setEmail("admin@e.com");
        dto.setRole("ADMINISTRATOR");

        var resp = new UserResponseDTO("A1", "CC", "Admin User", 123456789, "admin@e.com", "ADMINISTRATOR");
        Mockito.when(userService.createUser(any(UserRequestDTO.class))).thenReturn(resp);

        mvc.perform(post("/users/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("A1")))
                .andExpect(jsonPath("$.role", is("ADMINISTRATOR")));
    }

    @Test
    @DisplayName("POST /users/staff invalid → 400")
    void createStaffInvalid() throws Exception {
        // Missing fullName → violation of @NotBlank
        UserRequestDTO dto = new UserRequestDTO();
        dto.setId("A1");
        dto.setIdType("CC");
        // dto.setFullName(null); // Should trigger validation error

        mvc.perform(post("/users/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity()); // 422 from your handler
    }
}
