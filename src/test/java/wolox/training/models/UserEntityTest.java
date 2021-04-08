package wolox.training.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNeverOwnedException;
import wolox.training.models.UtilsForTest.UsersForTest;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserEntityTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Book aBookWithRandomCorrectFields() {
        Book book1 = new Book();
        book1.setGenre("Fantasy");
        book1.setAuthor("J. K. Rowling");
        book1.setImage("image.jpg");
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setSubtitle("-");
        book1.setPublisher("Bloomsbury Publishing");
        book1.setYear("1997");
        book1.setPages(223);
        book1.setIsbn("9780747532743");
        return book1;
    }

    public User anUserWithRandomCorrectFields() {
        User user1 = new User();
        user1.setName("Michella");
        user1.setUsername("Alis");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        return user1;
    }

    @Test
    void whenAnUserIsSavedWithoutName_thenItThrowsExceptionAndItIsNotSaved() {
        User user1 = new User();
        user1.setUsername("Alis");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        assertThrows(DataIntegrityViolationException.class, () ->
                this.userRepository.save(user1)
        );
    }

    @Test
    void whenAnUserIsSavedWithoutUserName_thenItThrowsExceptionAndItIsNotSaved() {
        User user1 = new User();
        user1.setName("Alis");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        assertThrows(DataIntegrityViolationException.class, () ->
                this.userRepository.save(user1)
        );
    }

    @Test
    void whenUserIsSavedWithoutBirthdayField_thenItThrowsExceptionAndItIsNotSaved() {
        User user1 = new User();
        user1.setName("Alis");
        user1.setUsername("Michella");
        assertThrows(DataIntegrityViolationException.class, () ->
                this.userRepository.save(user1)
        );
    }

    @Test
    void whenUserIsSavedWithoutBook_thenItIsSavedCorrectly() {
        User user1 = new User();
        user1.setName("Michella");
        user1.setUsername("Alis");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        User user2 = this.userRepository.save(user1);
        assertEquals(user2.getName(), user1.getName());
        assertEquals(user2.getUsername(), user1.getUsername());
        assertEquals(user2.getBirthdate(), user1.getBirthdate());
        assertSame(user2.getId(), user1.getId());

    }

    @Test
    void whenAddANewExistingBookToUser_thenItIsSavedInItsList() {
        User user = this.anUserWithRandomCorrectFields();
        Book book = this.aBookWithRandomCorrectFields();
        bookRepository.save(book);
        user.addBook(book);
        User user2 = userRepository.save(user);
        assertTrue(user2.getBooks().contains(book));
    }

    @Test
    void whenAddABookThatTheUserAlreadyHas_thenItThrowsAnException() {
        User user = this.anUserWithRandomCorrectFields();
        Book book = this.aBookWithRandomCorrectFields();
        bookRepository.save(book);
        user.addBook(book);
        userRepository.save(user);
        Exception exception = assertThrows(BookAlreadyOwnedException.class, () ->
                user.addBook(book)
        );
        assertEquals("The user already has that book", exception.getMessage());
    }

    @Test
    void whenRemoveABookThatTheUserHas_thenItIsRemovedCorrectly() {
        User user = this.anUserWithRandomCorrectFields();
        Book book = this.aBookWithRandomCorrectFields();
        user.addBook(book);
        user.removeBook(book);
        assertFalse(user.getBooks().contains(book));
    }

    @Test
    void whenRemoveABookThatTheUserHasNot_thenItThrowsAnException() {
        User user = this.anUserWithRandomCorrectFields();
        Book book = this.aBookWithRandomCorrectFields();
        Exception exception = assertThrows(BookNeverOwnedException.class, () ->
                user.removeBook(book)
        );
        assertEquals(
                "The user has not that book hence it can not be removed from the book list", exception.getMessage());
    }

    private List<User> filterUserByDatesAndName(LocalDate from, LocalDate to, String partOfTheName, List<User> users) {
        return users.stream().filter(
                user -> (to == null || user.getBirthdate().isBefore(to) || user.getBirthdate().equals(to))
                        && (from == null || user.getBirthdate().isAfter(from) || user.getBirthdate().equals(from))
                        && (partOfTheName == null || user.getUsername().toLowerCase()
                        .contains(partOfTheName.toLowerCase()))
        ).collect(Collectors.toList());
    }

    @Test
    void whenSearchingForUsersByDatesAndName_thenItRetrievesUsersUnderThoseConditions() {
        UsersForTest usersForTest = new UsersForTest();
        List<User> users = usersForTest.usersList();
        LocalDate from = LocalDate.of(1979, 9, 19);
        LocalDate to = LocalDate.of(1981, 2, 13);
        String partOfName = "H";
        users.forEach(user -> userRepository.save(user));
        List<User> resultQuery = userRepository
                .findByBirthdateBetweenAndNameIgnoreCaseContaining(from, to, partOfName);
        List<User> usersFilter = this.filterUserByDatesAndName(from, to, partOfName, users);
        assertSame(resultQuery.size(), usersFilter.size());
        assertTrue(usersFilter.containsAll(resultQuery));

    }

    @Test
    void whenSearchingForUsersByPartOfTheirNameWithNullFieldsDateFromAndDateTo_thenItRetrievesThem() {
        UsersForTest usersForTest = new UsersForTest();
        List<User> users = usersForTest.usersList();
        String partOfName = "A";
        users.forEach(user -> userRepository.save(user));

        List<User> resultQuery = userRepository
                .findUserByBirthdayBetweenAndContaining(null, null, partOfName);
        List<User> usersFilter = this.filterUserByDatesAndName(null, null, partOfName, users);
        assertSame(resultQuery.size(), usersFilter.size());
        assertTrue(usersFilter.containsAll(resultQuery));
    }

    @Test
    void whenSearchingForUsersByBirthdayBeforeADateWithNullFieldsDateFromAndPartOfTheirName_thenItRetrievesThem() {
        UsersForTest usersForTest = new UsersForTest();
        List<User> users = usersForTest.usersList();
        LocalDate to = LocalDate.of(1981, 2, 13);
        users.forEach(user -> userRepository.save(user));
        List<User> resultQuery = userRepository
                .findUserByBirthdayBetweenAndContaining(null, to, null);
        List<User> usersFilter = this.filterUserByDatesAndName(null, to, null, users);
        assertSame(resultQuery.size(), usersFilter.size());
        assertTrue(usersFilter.containsAll(resultQuery));
    }

    @Test
    void whenSearchingForUsersByBirthdayAfterADateWithNullFieldsDateToAndPartOfTheirName_thenItRetrievesThem() {
        UsersForTest usersForTest = new UsersForTest();
        List<User> users = usersForTest.usersList();
        LocalDate from = LocalDate.of(1981, 2, 13);
        users.forEach(user -> userRepository.save(user));
        List<User> resultQuery = userRepository
                .findUserByBirthdayBetweenAndContaining(from, null, null);
        List<User> usersFilter = this.filterUserByDatesAndName(from, null, null, users);
        assertSame(resultQuery.size(), usersFilter.size());
        assertTrue(usersFilter.containsAll(resultQuery));

    }
}
