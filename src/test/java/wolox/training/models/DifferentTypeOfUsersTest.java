package wolox.training.models;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.UtilsForTest.DifferentTypeOfUsersForTest;
import wolox.training.repositories.ProfessorRepository;
import wolox.training.repositories.StudentRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DifferentTypeOfUsersTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    private List<User> justUsersList;
    private List<Professor> professorList;
    private List<Student> studentList;
    private int totalUsers;

    @BeforeEach
    void before() {
        DifferentTypeOfUsersForTest differentTypeOfUsersForTest = new DifferentTypeOfUsersForTest();
        justUsersList = differentTypeOfUsersForTest.usersList();
        professorList = differentTypeOfUsersForTest.professorList();
        studentList = differentTypeOfUsersForTest.studentList();
        justUsersList.forEach(user -> userRepository.save(user));
        professorList.forEach(professor -> professorRepository.save(professor));
        studentList.forEach(student -> studentRepository.save(student));
        totalUsers = justUsersList.size() + studentList.size() + professorList.size();

    }

    @Test
    void whenSearchForAllTypeOfUsers_thenStudentsAreRetrievedWithItsCorrectTypeOfUserField() {
        Pageable pageable = PageRequest.of(0, totalUsers + 100);
        List<User> users = userRepository.findAll(pageable).toList();
        List<User> studentsUsers = users.stream().filter(user -> user.getTypeOfUser().equals("Student"))
                .collect(Collectors.toList());
        assertEquals(studentsUsers.size(),studentList.size());
    }

    @Test
    void whenSearchForAllTypeOfUsers_thenProfessorsAreRetrievedWithItsCorrectTypeOfUserField() {
        Pageable pageable = PageRequest.of(0, totalUsers + 100);
        List<User> users = userRepository.findAll(pageable).toList();
        List<User> professorsUsers = users.stream().filter(user -> user.getTypeOfUser().equals("Professor"))
                .collect(Collectors.toList());
        assertEquals(professorsUsers.size(),professorList.size());
    }

    @Test
    void whenSearchForAllTypeOfUsers_thenUsersAreRetrievedWithItsCorrectTypeOfUserField() {
        Pageable pageable = PageRequest.of(0, totalUsers + 100);
        List<User> allUsers = userRepository.findAll(pageable).toList();
        List<User> justUsers = allUsers.stream().filter(user -> user.getTypeOfUser().equals("User"))
                .collect(Collectors.toList());
        assertEquals(justUsers.size(),justUsersList.size());
    }

    @Test
    void whenSearchForAllTypeOfUsers_thenTheyAreRetrieved() {
        int totalUsers = justUsersList.size() + studentList.size() + professorList.size();
        Pageable pageable = PageRequest.of(0, totalUsers + 100);
        List<User> users = userRepository.findAll(pageable).toList();
        assertEquals(totalUsers, users.size());
        assertTrue(users.containsAll(professorList));
        assertTrue(users.containsAll(studentList));
        assertTrue(users.containsAll(justUsersList));
    }

    @Test
    void whenSearchForStudents_thenTheyAreRetrieved() {
        int totalStudents = studentList.size();
        Pageable pageable = PageRequest.of(0, totalStudents + 100);
        List<Student> students = studentRepository.findAll(pageable).toList();
        assertEquals(totalStudents, students.size());
        assertTrue(students.containsAll(studentList));
    }

    @Test
    void whenSearchForProfessors_thenTheyAreRetrieved() {
        int totalProfessors = professorList.size();
        Pageable pageable = PageRequest.of(0, totalProfessors + 100);
        List<Professor> professors = professorRepository.findAll(pageable).toList();
        assertEquals(totalProfessors, professors.size());
        assertTrue(professors.containsAll(professorList));
    }

}