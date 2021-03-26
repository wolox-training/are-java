package wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNeverOwnedException;

@Entity(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @ApiModelProperty(notes = "The date must be dd/MM/yyyy")
    private LocalDate birthdate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_user",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"))
    private List<Book> books = new ArrayList<>();

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = checkNotNull(username);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = checkNotNull(name);
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = checkNotNull(birthdate);
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = checkNotNull(books);
    }


    /**
     * The book is added if it exists and if the user doesn't already have it
     *
     * @param book: The book to be added to the user's book list (Book)
     */
    public void addBook(Book book) {
        if (this.books.stream().anyMatch(book1 -> book1.getId() == book.getId())) {
            throw new BookAlreadyOwnedException();
        }
        this.books.add(book);
    }

    /**
     * The book is removed if it exists and if the user already has it
     *
     * @param book: The book to be removed from the user's book list (Book)
     */
    public void removeBook(Book book) {
        if (!this.books.remove(book)) {
            throw new BookNeverOwnedException();
        }
    }
}
