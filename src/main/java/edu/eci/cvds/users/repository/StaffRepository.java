package edu.eci.cvds.users.repository;

import edu.eci.cvds.users.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository <Staff, String>{
}
