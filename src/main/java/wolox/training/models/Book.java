package wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String genre;

    @Column(nullable = false)
    private String author;
    @Column
    @Setter @NonNull
    private String image;

    @Column(nullable = false)
    @Setter @NonNull
    private String title;

    @Column(nullable = false)
    @Setter @NonNull
    private String subtitle;

    @Column(nullable = false)
    @Setter @NonNull
    private String publisher;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private Integer pages;

    @Column(nullable = false)
    private Long isbn;

    public Book() {
    }

    public void setGenre(String genre) {
        Preconditions.checkArgument(genre.length() > 1, "The genre field must have more than 1 character");
        this.genre = checkNotNull(genre);
        ;
    }

    public void setAuthor(String author) {
        Preconditions.checkArgument(!author.isEmpty(), "The author name cannot be empty");
        this.author = author;
    }

    public void setYear(String year) {
        Preconditions.checkArgument(year.matches("[0-9]+"), "The year field must contain only numbers");
        Preconditions.checkArgument(Integer.valueOf(year) <= LocalDateTime.now().getYear(),
                "The year field must be less than or equal to the current year");
        this.year = checkNotNull(year);
    }

    public void setPages(Integer pages) {
        this.pages = checkNotNull(pages);
    }

    public void setIsbn(Long isbn) {
        Preconditions.checkArgument(year.matches("[0-9]+"), "The isbn field must contain only numbers");
        this.isbn = checkNotNull(isbn);
    }
}
