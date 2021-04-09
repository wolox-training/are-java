package wolox.training.models.UtilsForTest;

import java.util.Arrays;
import java.util.List;
import wolox.training.models.Book;

public class BooksForTest {

    public List<Book> books() {

        Book book1 = new Book();
        book1.setGenre("Fantasy");
        book1.setAuthor("J. K. Rowling");
        book1.setImage("image.jpg");
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setSubtitle("-");
        book1.setPublisher("Bloomsbury Publishing");
        book1.setYear("1997");
        book1.setPages(223);
        book1.setIsbn(9780747532743L);

        Book book2 = new Book();
        book2.setGenre("Fantasy");
        book2.setAuthor("J. K. Rowling");
        book2.setImage("image.jpg");
        book2.setTitle("Harry Potter and the Deathly Hallows");
        book2.setSubtitle("-");
        book2.setPublisher("Bloomsbury Publishing");
        book2.setYear("2007");
        book2.setPages(607);
        book2.setIsbn(9780739360385L);

        Book book3 = new Book();
        book3.setGenre("Fantasy");
        book3.setAuthor("J. K. Rowling");
        book3.setImage("image.jpg");
        book3.setTitle("Order of the Phoenix");
        book3.setSubtitle("-");
        book3.setPublisher("Bloomsbury Publishing");
        book3.setYear("2003");
        book3.setPages(766);
        book3.setIsbn(9781855496484L);

        Book book4 = new Book();
        book4.setGenre("Horror");
        book4.setAuthor("Edgar Allan Poe");
        book4.setImage("image.jpg");
        book4.setTitle("The Tell-Tale Heart");
        book4.setSubtitle("-");
        book4.setPublisher("The Pioneer");
        book4.setYear("1843");
        book4.setPages(68);
        book4.setIsbn(9781976158391L);

        Book book5 = new Book();
        book5.setGenre("Fantasy");
        book5.setAuthor("Imaginary");
        book5.setImage("image.jpg");
        book5.setTitle("An imaginary title");
        book5.setSubtitle("-");
        book5.setPublisher("Bloomsbury Publishing");
        book5.setYear("1997");
        book5.setPages(1);
        book5.setIsbn(9781976158391L);

        return Arrays.asList(book1, book2, book3, book4, book5);
    }
}
