package edu.eci.cvds.users.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.cvds.users.controller.UserController;
import edu.eci.cvds.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserExceptionTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /users/{id} → 400 BadRequestException manejada")
    void handleBadRequestException() throws Exception {
        Mockito.when(userService.getUserById("bad-id"))
                .thenThrow(new BadRequestException("Invalid ID format"));

        mvc.perform(get("/users/bad-id"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid ID format"))
                .andExpect(jsonPath("$.path", containsString("/users/bad-id")));
    }

    @Test
    @DisplayName("GET /users/{id} → 500 Exception genérica manejada")
    void handleGenericException() throws Exception {
        Mockito.when(userService.getUserById("crash"))
                .thenThrow(new RuntimeException("Unexpected crash"));

        mvc.perform(get("/users/crash"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andExpect(jsonPath("$.path", containsString("/users/crash")));
    }
}
