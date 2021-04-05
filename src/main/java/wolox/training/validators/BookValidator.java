package wolox.training.validators;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.exceptions.NullFieldException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Component
public class BookValidator {

    @Autowired
    BookRepository bookRepository;

    public void existsId(Long id) {
        bookRepository.findById(id).orElseThrow(IdNotFoundException::new);
    }

    public void idsMatchAndExist(Book book, Long id) {
        if (book.getId() != id) {
            throw new IdMismatchException();
        }
        this.existsId(id);
    }

    public void validateFields(Book book) {
        try {
            Preconditions.checkNotNull(book.getAuthor(), "Author must have a value");
            Preconditions.checkNotNull(book.getImage(), "Image must have a value");
            Preconditions.checkNotNull(book.getTitle(), "Title must have a value");
            Preconditions.checkNotNull(book.getSubtitle(), "Subtitle must have a value");
            Preconditions.checkNotNull(book.getPublisher(), "Publisher must have a value");
            Preconditions.checkNotNull(book.getYear(), "Year must have a value");
            Preconditions.checkNotNull(book.getPages(), "Pages must have a value");
            Preconditions.checkNotNull(book.getIsbn(), "Isbn must have a value");

        } catch (NullPointerException e) {
            throw new NullFieldException(e);
        }
    }
}
