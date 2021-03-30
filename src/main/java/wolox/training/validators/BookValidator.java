package wolox.training.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.IdNotFoundException;
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
}
