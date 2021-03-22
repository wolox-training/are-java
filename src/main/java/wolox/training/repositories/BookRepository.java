package wolox.training.repositories;

import java.util.List;
import org.springframework.data.repository.Repository;
import wolox.training.models.Book;

public interface BookRepository extends Repository<Book, Long> {

  List<Book> findByAuthor(String author);

}
