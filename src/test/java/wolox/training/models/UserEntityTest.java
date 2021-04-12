package wolox.training.models;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNeverOwnedException;
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
        assertThrows(DataIntegrityViolationException.class, () -> {
            this.userRepository.save(user1);
        });
    }

    @Test
    void whenAnUserIsSavedWithoutUserName_thenItThrowsExceptionAndItIsNotSaved() {
        User user1 = new User();
        user1.setName("Alis");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        assertThrows(DataIntegrityViolationException.class, () -> {
            this.userRepository.save(user1);
        });
    }

    @Test
    void whenUserIsSavedWithoutBirthdayField_thenItThrowsExceptionAndItIsNotSaved() {
        User user1 = new User();
        user1.setName("Alis");
        user1.setUsername("Michella");
        assertThrows(DataIntegrityViolationException.class, () -> {
            this.userRepository.save(user1);
        });
    }

    @Test
    void whenUserIsSavedWithoutBook_thenItIsSavedCorrectly() {
        User user1 = new User();
        user1.setName("Michella");
        user1.setUsername("Alis");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        User user2 = this.userRepository.save(user1);
        assertTrue(user2.getName().equals(user1.getName()));
        assertTrue(user2.getUsername().equals(user1.getUsername()));
        assertTrue(user2.getBirthdate().equals(user1.getBirthdate()));
        assertTrue(user2.getId() == user1.getId());

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
        Exception exception = assertThrows(BookAlreadyOwnedException.class, () -> {
            user.addBook(book);
            ;
        });
        assertTrue(exception.getMessage().equals("The user already has that book"));
    }

    @Test
    void whenRemoveABookThatTheUserHas_thenItIsRemovedCorrectly() {
        User user = this.anUserWithRandomCorrectFields();
        Book book = this.aBookWithRandomCorrectFields();
        user.addBook(book);
        user.removeBook(book);
        assertTrue(!user.getBooks().contains(book));
    }

    @Test
    void whenRemoveABookThatTheUserHasNot_thenItThrowsAnException() {
        User user = this.anUserWithRandomCorrectFields();
        Book book = this.aBookWithRandomCorrectFields();
        Exception exception = assertThrows(BookNeverOwnedException.class, () -> {
            user.removeBook(book);
        });
        assertTrue(exception.getMessage()
                .equals("The user has not that book hence it can not be removed from the book list"));
    }

}
