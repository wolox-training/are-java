package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;
import wolox.training.models.Book;

public interface BookRepository extends Repository<Book, Long> {

  Optional<Book> findFirstByAuthor(String author);

}
