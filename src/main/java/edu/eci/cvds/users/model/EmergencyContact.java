package edu.eci.cvds.users.model;

import jakarta.persistence.*;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, length = 10)
    private Integer phone;

    @Column(name = "idType", nullable = false, length = 20)
    private String idType;

    @Column(name = "id_number", nullable = false, length = 20)
    private String idNumber;

    @Column(nullable = false)
    private String relationship;

    public EmergencyContact() {}

    public EmergencyContact(String fullName, Integer phone,
                            String idType, String idNumber,
                            String relationship) {
        this.fullName = fullName;
        this.phone = phone;
        this.idType = idType;
        this.idNumber = idNumber;
        this.relationship = relationship;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
