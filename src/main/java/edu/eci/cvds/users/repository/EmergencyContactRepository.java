package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
}
