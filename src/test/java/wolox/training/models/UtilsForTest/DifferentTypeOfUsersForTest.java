package wolox.training.models.UtilsForTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import wolox.training.models.Professor;
import wolox.training.models.Student;
import wolox.training.models.User;

public class DifferentTypeOfUsersForTest {

    public List<User> usersList() {
        User user1 = new User();
        user1.setName("Alison");
        user1.setUsername("Alison");
        user1.setBirthdate(LocalDate.of(1997, 05, 23));
        user1.setPassword("1234");

        User user2= new User();
        user2.setName("Molly");
        user2.setUsername("Weasley");
        user2.setBirthdate(LocalDate.of(1950, 10, 30));
        user2.setPassword("Caretaker");

        User user3= new User();
        user3.setName("Sirius");
        user3.setUsername("Black");
        user3.setBirthdate(LocalDate.of(1959, 11, 3));
        user3.setPassword("Friendship");

        User user4= new User();
        user4.setName("Dobby");
        user4.setUsername("Dobby");
        user4.setBirthdate(LocalDate.of(1700, 6, 28));
        user4.setPassword("Free");

        return Collections.unmodifiableList(Arrays.asList(user1, user2, user3, user4));

    }
    public List<Student>studentList(){
        Student student1 = new Student();
        student1.setName("Luna");
        student1.setUsername("Luna");
        student1.setBirthdate(LocalDate.of(1981, 2, 13));
        student1.setPassword("Nargles");
        student1.setYear(6);

        Student student2 = new Student();
        student2.setName("Harry");
        student2.setUsername("Harry");
        student2.setBirthdate(LocalDate.of(1980, 7, 31));
        student2.setPassword("expectoPatronum");
        student2.setYear(6);


        Student student3 = new Student();
        student3.setName("Hermione");
        student3.setUsername("Hermione");
        student3.setBirthdate(LocalDate.of(1979, 9, 19));
        student3.setPassword("library");
        student3.setYear(6);


        Student student4 = new Student();
        student4.setName("Ron");
        student4.setUsername("Ron");
        student4.setBirthdate(LocalDate.of(1980, 3, 1));
        student4.setPassword("Spiders");
        student4.setYear(6);

        return Collections.unmodifiableList(Arrays.asList(student1, student2, student3, student4));

    }

    public List<Professor> professorList(){
        Professor professor= new Professor();
        professor.setName("Minerva");
        professor.setUsername("McGonagall");
        professor.setBirthdate(LocalDate.of(1935, 10, 4));
        professor.setPassword("Cat");
        professor.setSubject("Transfiguration");


        Professor professor1= new Professor();
        professor1.setName("Albus");
        professor1.setUsername("Dumbledore");
        professor1.setBirthdate(LocalDate.of(1881, 7, 1));
        professor1.setPassword("Phoenix");
        professor1.setSubject("Transfiguration");

        Professor professor2= new Professor();
        professor2.setName("Severus");
        professor2.setUsername("Snape");
        professor2.setBirthdate(LocalDate.of(1960, 6, 9));
        professor2.setPassword("Always");
        professor2.setSubject("Defense against the dark arts");

        return Collections.unmodifiableList(Arrays.asList(professor, professor1, professor2));

    }

}
