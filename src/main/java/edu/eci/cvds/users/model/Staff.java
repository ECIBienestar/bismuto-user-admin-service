package edu.eci.cvds.users.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "staff")
public class Staff extends User{
    @Column(nullable = true)
    private String specialty; //only for MEDICAL_STAFF

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id")
    private List<ExternalScheduleEntry> availableSchedule;

    protected Staff() {}

    public Staff(String id, String idType, String fullName, Integer phone,
                 String email, Role role, String specialty,
                 List<ExternalScheduleEntry> availableSchedule) {
        super(id, idType, fullName, phone, email, role);
        this.specialty = specialty;
        this.availableSchedule = availableSchedule;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<ExternalScheduleEntry> getAvailableSchedule() {
        return availableSchedule;
    }

    public void setAvailableSchedule(List<ExternalScheduleEntry> availableSchedule) {
        this.availableSchedule = availableSchedule;
    }
}
