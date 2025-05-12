package edu.eci.cvds.users.model;

import edu.eci.cvds.users.model.enums.IdType;
import edu.eci.cvds.users.model.enums.Role;
import jakarta.persistence.Entity;

@Entity
public class TestUser extends User {
    public TestUser() {
    }

    public TestUser(String id, IdType idType, String fullName, String phone,
                    String email, Role role, String password) {
        this.setId(id);
        this.setIdType(idType);
        this.setFullName(fullName);
        this.setPhone(phone);
        this.setEmail(email);
        this.setRole(role);
        this.setPassword(password);
        this.setActive(true);
    }
}