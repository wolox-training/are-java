package wolox.training.repositories;


import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findFirstByAuthor(String author);

    Optional<Book> findFirstByIsbn(String isbn);

    Page<Book> findByPublisherAndYearAndGenre(String publisher, String year, String genre, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE (:publisher is null or b.publisher = :publisher) and (:year is null"
            + " or b.year = :year) and (:genre is null or b.genre = :genre)")
    Page<Book> findBookByPublisherAndYearAndGenre(@Param("publisher") String publisher, @Param("year") String year,
            @Param("genre") String genre, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE "
            + "(:publisher is null or publisher='' or b.publisher = :publisher)"
            + " and (:year is null or :year='' or b.year = :year)"
            + " and (:genre is null or :genre='' or b.genre = :genre)"
            + " and (:author is null or :author='' or b.author = :author)"
            + " and (:isbn is null or :isbn='' or b.isbn = :isbn)"
            + " and (:image is null or :image='' or b.image = :image)"
            + " and (:title is null or :title='' or b.title = :title)"
            + " and (:subtitle is null or :subtitle='' or b.subtitle = :subtitle)"
            + " and (:pages is null or b.pages = :pages)"
    )
    Page<Book> findBooksBy(
            @Param("isbn") String isbn,
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("image") String image,
            @Param("title") String title,
            @Param("subtitle") String subtitle,
            @Param("publisher") String publisher,
            @Param("year") String year,
            @Param("pages") Integer pages,
            Pageable pageable
    );

}
