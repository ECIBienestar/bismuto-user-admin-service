package edu.eci.cvds.users.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student extends User {
    @Column(name = "student_code", nullable = false, unique = true)
    private String studentCode;

    @Column(nullable = false)
    private String program;

    @Column(nullable = false)
    private int semester;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emergency_contact_id")
    private EmergencyContact emergencyContact;

    @Column(nullable = false)
    private String address;

    protected Student() {
    }

    public Student(String id, String idType, String fullName,
                   Integer phone, String email, Role role,
                   String studentCode, String program, LocalDate birthDate,
                   EmergencyContact emergencyContact, String address) {
        super(id, idType, fullName, phone, email, role);
        this.studentCode = studentCode;
        this.program = program;
        this.birthDate = birthDate;
        this.emergencyContact = emergencyContact;
        this.address = address;
    }

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

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
