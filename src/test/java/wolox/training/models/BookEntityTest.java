package wolox.training.models;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.UtilsForTest.BooksForTest;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookEntityTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void bookCannotBeSavedWithNullAuthor() {
        Book book1 = new Book();
        book1.setGenre("Fantasy");
        book1.setImage("image.jpg");
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setSubtitle("-");
        book1.setPublisher("Bloomsbury Publishing");
        book1.setYear("1997");
        book1.setPages(223);
        book1.setIsbn("9780747532743");

        assertThrows(DataIntegrityViolationException.class, () ->
            bookRepository.save(book1)
        );
    }

    @Test
    void whenBookHasJust1CharInGenreField_thenItThrowsExceptionWithCustomErrorMessage() {
        Book book1 = new Book();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> book1.setGenre("F"));
        assertTrue(exception.getMessage().contains("The genre field must have more than 1 character"));
    }

    @Test
    void whenBookHasAnEmptyAuthorField_thenItThrowsException() {
        Book book1 = new Book();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                book1.setAuthor("")
        );
        assertTrue(exception.getMessage().contains("The author name cannot be empty"));
    }

    @Test
    void whenBookHasYearFieldGreaterThanTheCurrentYear_thenItThrowsException() {
        Book book1 = new Book();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            book1.setYear("4444")
        );
        assertTrue(exception.getMessage().contains("The year field must be less than or equal to the current year"));
    }


    @Test
    void whenBookHasLettersInTheYearField_thenItThrowsException() {
        Book book1 = new Book();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            book1.setYear("f4444")
        );
        assertTrue(exception.getMessage().contains("The year field must contain only numbers"));
    }

    @Test
    void whenBookItIsSaved_thenYouCanGetItBackWithId() {
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
        book1 = bookRepository.save(book1);
        Book book2 = bookRepository.findById(book1.getId()).get();
        assertEquals(book1.getId() , book2.getId());
        assertEquals(book1.getAuthor() , book2.getAuthor());
        assertEquals(book1.getImage() , book2.getImage());
        assertEquals(book1.getYear() , book2.getYear());
        assertEquals(book1.getPages() , book2.getPages());
        assertEquals(book1.getIsbn() , book2.getIsbn());
        assertEquals(book1.getPublisher() , book2.getPublisher());
        assertEquals(book1.getSubtitle() , book2.getSubtitle());
        assertEquals(book1.getTitle() , book2.getTitle());
    }

    private List<Book> filterBooksByYearPublisherAndGenre(String year, String publisher, String genre,
            List<Book> bookList) {
        return bookList.stream().filter(book -> (publisher == null || book.getPublisher().equals(publisher))
                && (year == null || book.getYear().equals(year))
                && (genre == null || book.getGenre().equals(genre))).collect(Collectors.toList());

    }

    @Test
    void whenSearchingBooksByYearPublisherAndGenre_thenItRetrievesBooksUnderThoseConditions() {
        BooksForTest booksForTest = new BooksForTest();
        String year = "1997";
        String publisher = "Bloomsbury Publishing";
        String genre = "Fantasy";
        List<Book> bookList = booksForTest.books();
        bookList.forEach(book -> bookRepository.save(book));
        List<Book> filterBooks = this.filterBooksByYearPublisherAndGenre(year, publisher, genre, bookList);
        List<Book> responseBooks = this.bookRepository.findByPublisherAndYearAndGenre(publisher, year, genre);
        assertSame(filterBooks.size(), responseBooks.size());
        assertTrue(responseBooks.containsAll(filterBooks));
    }


    @Test
    void whenSearchingForABookByYearWithNullPublisherAndGenre_thenItRetrievesUserUnderThoseConditions() {
        BooksForTest booksForTest = new BooksForTest();
        String year = "1997";
        List<Book> bookList = booksForTest.books();
        bookList.forEach(book -> bookRepository.save(book));
        List<Book> filterBooks = this.filterBooksByYearPublisherAndGenre(year, null, null, bookList);
        List<Book> responseBooks = this.bookRepository.findBookByPublisherAndYearAndGenre(null, year, null);
        assertSame(filterBooks.size(), responseBooks.size());
        assertTrue(responseBooks.containsAll(filterBooks));
    }

    @Test
    void whenSearchingForABookByPublisherWithNullYearAndGenre_thenItRetrievesUserUnderThoseConditions() {
        BooksForTest booksForTest = new BooksForTest();
        String publisher = "Bloomsbury Publishing";
        List<Book> bookList = booksForTest.books();
        bookList.forEach(book -> bookRepository.save(book));
        List<Book> filterBooks = this.filterBooksByYearPublisherAndGenre(null, publisher, null, bookList);
        List<Book> responseBooks = this.bookRepository.findBookByPublisherAndYearAndGenre(publisher, null, null);
        assertSame(filterBooks.size(), responseBooks.size());
        assertTrue(responseBooks.containsAll(filterBooks));
    }

    @Test
    void whenSearchingForABookByGenreWithNullYearAndPublisher_thenItRetrievesUserUnderThoseConditions() {
        BooksForTest booksForTest = new BooksForTest();
        String genre = "Fantasy";
        List<Book> bookList = booksForTest.books();
        bookList.forEach(book -> bookRepository.save(book));
        List<Book> filterBooks = this.filterBooksByYearPublisherAndGenre(null, null, genre, bookList);
        List<Book> responseBooks = this.bookRepository.findBookByPublisherAndYearAndGenre(null, null, genre);
        assertSame(filterBooks.size(), responseBooks.size());
        assertTrue(responseBooks.containsAll(filterBooks));
    }
}
