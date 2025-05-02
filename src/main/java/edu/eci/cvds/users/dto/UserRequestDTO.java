package edu.eci.cvds.users.dto;

import jakarta.validation.constraints.*;

public class UserRequestDTO {
    @NotBlank @Size(max = 50)
    private String id;

    @NotBlank @Size(max = 20)
    private String idType;

    @NotBlank
    private String fullName;

    @NotNull
    private Integer phone;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String role;  // Ej. "ADMINISTRATOR"

    // getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
