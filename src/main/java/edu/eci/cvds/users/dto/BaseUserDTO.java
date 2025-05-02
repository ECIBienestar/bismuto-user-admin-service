package edu.eci.cvds.users.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUserDTO {

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
    private String role;
}
