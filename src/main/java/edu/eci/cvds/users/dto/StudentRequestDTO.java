package edu.eci.cvds.users.dto;

import edu.eci.cvds.users.model.User;
import jakarta.validation.constraints.*;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Payload DTO for creating or updating a Student.
 * Hereda de UserRequestDTO todos los campos comunes.
 */
@Getter @Setter
public class StudentRequestDTO extends UserRequestDTO {
    @NotBlank
    private String studentCode;

    @NotBlank
    private String program;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    private String address;

    @NotBlank
    private String emergencyContactId;

    public String getStudentCode() {
        return studentCode;
    }
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getProgram() {
        return program;
    }
    public void setProgram(String program) {
        this.program = program;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContactId() {
        return emergencyContactId;
    }
    public void setEmergencyContactId(String emergencyContactId) {
        this.emergencyContactId = emergencyContactId;
    }
}
