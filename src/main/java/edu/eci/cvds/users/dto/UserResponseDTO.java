package edu.eci.cvds.users.dto;

public class UserResponseDTO extends BaseUserDTO {

    public UserResponseDTO() {}

    public UserResponseDTO(String id, String idType, String fullName,
                           Integer phone, String email, String role) {
        setId(id);
        setIdType(idType);
        setFullName(fullName);
        setPhone(phone);
        setEmail(email);
        setRole(role);
    }
}
