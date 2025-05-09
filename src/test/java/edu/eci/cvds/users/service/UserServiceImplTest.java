package edu.eci.cvds.users.service;

import edu.eci.cvds.users.dto.StudentRequestDTO;
import edu.eci.cvds.users.dto.UserRequestDTO;
import edu.eci.cvds.users.dto.UserResponseDTO;
import edu.eci.cvds.users.exception.ResourceNotFoundException;
import edu.eci.cvds.users.model.*;
import edu.eci.cvds.users.model.enums.Role;
import edu.eci.cvds.users.repository.*;
import edu.eci.cvds.users.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private StudentRepository studentRepo;
    @Mock private StaffRepository staffRepo;
    @Mock private EmergencyContactRepository contactRepo;

    @InjectMocks
    private UserServiceImpl service;

    private EmergencyContact sampleContact;

    @BeforeEach
    void setUp() {
        sampleContact = new EmergencyContact("Pepito Perez", 123456789,
                "CC", "123456789", "Cousin");
    }

    @Test
    void createStudent_success() {
        // dado un DTO válido
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setId("S1");
        dto.setIdType("CC");
        dto.setFullName("Student One");
        dto.setPhone(3000000);
        dto.setEmail("stud@uni.edu");
        dto.setStudentCode("STU100");
        dto.setProgram("Systems Engineering");
        dto.setBirthDate(LocalDate.of(2000,1,1));
        dto.setAddress("Somewhere");
        dto.setEmergencyContactId(4654L);

        when(contactRepo.findById(4654L)).thenReturn(Optional.of(sampleContact));

        // al crear
        UserResponseDTO resp = service.createStudent(dto);

        // verifica repo.save y valores del DTO
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepo).save(captor.capture());
        Student saved = captor.getValue();
        assertEquals("STU100", saved.getStudentCode());

        assertEquals("Student One", resp.getFullName());
        assertEquals("STUDENT", resp.getRole());
    }

    @Test
    void createStudent_missingContact_throws() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setEmergencyContactId(21231L);
        when(contactRepo.findById(21231L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createStudent(dto));
    }

    @Test
    void createUser_success() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setId("ST1");
        dto.setIdType("CC");
        dto.setFullName("Staff One");
        dto.setPhone(4000000);
        dto.setEmail("staff@uni.edu");
        dto.setRole("ADMINISTRATOR");

        UserResponseDTO resp = service.createUser(dto);

        // no lanza excepción y mapea correctamente
        assertEquals("Staff One", resp.getFullName());
        assertEquals("ADMINISTRATOR", resp.getRole());
    }

    @Test
    void getUserById_asStudent() {
        Student s = new Student(
                "S2", "CC", "Stud Two", 111, "s2@uni.edu",
                Role.WELLNESS_STAFF, "SC2", "Prog", LocalDate.now(),
                sampleContact, "Addr"
        );
        when(studentRepo.findById("S2")).thenReturn(Optional.of(s));

        UserResponseDTO resp = service.getUserById("S2");
        assertEquals("Stud Two", resp.getFullName());
    }

    @Test
    void getUserById_asStaff() {
        when(studentRepo.findById("X")).thenReturn(Optional.empty());
        Staff st = new Staff(
                "X", "CC", "Staff X", 222, "x@uni.edu",
                Role.TRAINER, "Spec", List.of()
        );
        when(staffRepo.findById("X")).thenReturn(Optional.of(st));

        UserResponseDTO resp = service.getUserById("X");
        assertEquals("Staff X", resp.getFullName());
    }

    @Test
    void getUserById_notFound_throws() {
        when(studentRepo.findById("Z")).thenReturn(Optional.empty());
        when(staffRepo.findById("Z")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.getUserById("Z"));
    }

    @Test
    void getAllUsers_combinesBoth() {
        EmergencyContact ec = new EmergencyContact(
                "EC1", 12332415, "Contact",
                "1234567", "Relation"
        );

        Student s = new Student(
                "A", "T", "NameA", 1, "a@e",
                Role.ADMINISTRATOR,
                "STU1", "Prog", LocalDate.of(2000,1,1),
                ec, "Addr"
        );

        Staff st = new Staff(
                "B", "T", "NameB", 2, "b@e",
                Role.TRAINER,
                "Specialty", List.of()
        );

        when(studentRepo.findAll()).thenReturn(List.of(s));
        when(staffRepo.findAll()).thenReturn(List.of(st));

        List<UserResponseDTO> all = service.getAllUsers();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(u -> u.getFullName().equals("NameA")));
        assertTrue(all.stream().anyMatch(u -> u.getFullName().equals("NameB")));
    }


    @Test
    void deleteUserById_student() {
        when(studentRepo.existsById("S1")).thenReturn(true);
        service.deleteUserById("S1");
        verify(studentRepo).deleteById("S1");
    }

    @Test
    void deleteUserById_staff() {
        when(studentRepo.existsById("S2")).thenReturn(false);
        when(staffRepo.existsById("S2")).thenReturn(true);
        service.deleteUserById("S2");
        verify(staffRepo).deleteById("S2");
    }

    @Test
    void deleteUserById_notFound_throws() {
        when(studentRepo.existsById("X")).thenReturn(false);
        when(staffRepo.existsById("X")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> service.deleteUserById("X"));
    }
}
