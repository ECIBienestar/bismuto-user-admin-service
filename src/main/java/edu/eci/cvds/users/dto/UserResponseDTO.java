package edu.eci.cvds.users.dto;

public class UserResponseDTO {
    private String id;
    private String idType;
    private String fullName;
    private Integer phone;
    private String email;
    private String role;

    public UserResponseDTO() {}

    public UserResponseDTO(String id, String idType, String fullName,
                           Integer phone, String email, String role) {
        this.id = id;
        this.idType = idType;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

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
