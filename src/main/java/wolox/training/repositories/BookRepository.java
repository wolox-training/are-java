package wolox.training.repositories;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findFirstByAuthor(String author);

    Optional<Book> findFirstByIsbn(String isbn);

    Optional<List<Book>> findByPublisherAndYearAndGenre(String publisher, String year, String genre);

    @Query("SELECT b FROM Book b WHERE (:publisher is null or b.publisher = :publisher) and (:year is null"
            + " or b.year = :year) and (:genre is null or b.genre = :genre)")
    List<Book> findBookByPublisherAndYearAndGenre(@Param("publisher") String publisher, @Param("year") String year,
            @Param("genre") String genre);

}
