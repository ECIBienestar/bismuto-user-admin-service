package edu.eci.cvds.users.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    @Test
    void gettersAndSettersWork() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId("U1");
        dto.setIdType("CC");
        dto.setFullName("User One");
        dto.setPhone(1234);
        dto.setEmail("u@e.com");
        dto.setRole("ADMINISTRATOR");

        assertEquals("U1", dto.getId());
        assertEquals("CC", dto.getIdType());
        assertEquals("User One", dto.getFullName());
        assertEquals(1234, dto.getPhone());
        assertEquals("u@e.com", dto.getEmail());
        assertEquals("ADMINISTRATOR", dto.getRole());
    }
}
