package wolox.training.repositories;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findFirstByAuthor(String author);

    Optional<Book> findFirstByIsbn(Long isbn);

    List<Book> findByPublisherAndYearAndGenre(String publisher, String year, String genre);

    @Query("SELECT b FROM Book b WHERE (:publisher is null or b.publisher = :publisher) and (:year is null"
            + " or b.year = :year) and (:genre is null or b.genre = :genre)")
    List<Book> findBookByPublisherAndYearAndGenre(@Param("publisher") String publisher, @Param("year") String year,
            @Param("genre") String genre);

    @Query("SELECT b FROM Book b WHERE "
            + "(:publisher is null or publisher='' or b.publisher = :publisher)"
            + " and (:year is null or :year='' or b.year = :year)"
            + " and (:genre is null or :genre='' or b.genre = :genre)"
            + " and (:author is null or :author='' or b.author = :author)"
            + " and (:isbn is null  or b.isbn = :isbn)"
            + " and (:image is null or :image='' or b.image = :image)"
            + " and (:title is null or :title='' or b.title = :title)"
            + " and (:subtitle is null or :subtitle='' or b.subtitle = :subtitle)"
            + " and (:pages is null or b.pages = :pages)"
    )
    List<Book> findBooksBy(
            @Param("isbn") Long isbn,
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("image") String image,
            @Param("title") String title,
            @Param("subtitle") String subtitle,
            @Param("publisher") String publisher,
            @Param("year") String year,
            @Param("pages") Integer pages
    );

}
