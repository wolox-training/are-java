package wolox.training.models;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookEntityTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void bookCannotBeSavedWithNullAuthor() {
        Book book1 = new Book();
        book1.setGenre("Fantasy");
        book1.setImage("image.jpg");
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setSubtitle("-");
        book1.setPublisher("Bloomsbury Publishing");
        book1.setYear("1997");
        book1.setPages(223);
        book1.setIsbn("9780747532743");

        assertThrows(DataIntegrityViolationException.class, () -> {
            bookRepository.save(book1);
        });
    }

    @Test
    public void whenBookHasJust1CharInGenreField_thenItThrowsExceptionWithCustomErrorMessage() {
        Book book1 = new Book();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            book1.setGenre("F");
        });
        assertTrue(exception.getMessage().contains("The genre field must have more than 1 character"));
    }

    @Test
    public void whenBookHasAnEmptyAuthorField_thenItThrowsException() {
        Book book1 = new Book();
        Exception exception=assertThrows(IllegalArgumentException.class, () -> {
            book1.setAuthor("");
        });
        assertTrue(exception.getMessage().contains("The author name cannot be empty"));
    }

    @Test
    public void whenBookHasYearFieldGreaterThanTheCurrentYear_thenItThrowsException() {
        Book book1 = new Book();
        Exception exception=assertThrows(IllegalArgumentException.class, () -> {
            book1.setYear("4444");
        });
        assertTrue(exception.getMessage().contains("The year field must be less than or equal to the current year"));
    }


    @Test
    public void whenBookHasLettersInTheYearField_thenItThrowsException() {
        Book book1 = new Book();
        Exception exception=assertThrows(IllegalArgumentException.class, () -> {
            book1.setYear("f4444");
        });
        assertTrue(exception.getMessage().contains("The year field must contain only numbers"));
    }

    @Test
    public void whenBookItIsSaved_thenYouCanGetItBackWithId(){
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
        book1=bookRepository.save(book1);
        Book book2=bookRepository.findById(book1.getId()).get();
        assertTrue(book1.getId()==book2.getId());
        assertTrue(book1.getAuthor()==book2.getAuthor());
        assertTrue(book1.getImage()==book2.getImage());
        assertTrue(book1.getYear()==book2.getYear());
        assertTrue(book1.getPages()==book2.getPages());
        assertTrue(book1.getIsbn()==book2.getIsbn());
        assertTrue(book1.getPublisher()==book2.getPublisher());
        assertTrue(book1.getSubtitle()==book2.getSubtitle());
        assertTrue(book1.getTitle()==book2.getTitle());
    }

}