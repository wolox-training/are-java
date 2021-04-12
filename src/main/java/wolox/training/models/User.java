package wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.base.Preconditions;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNeverOwnedException;

@Getter
@Entity(name = "USERS")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @ApiModelProperty(notes = "The date must be dd/MM/yyyy")
    private LocalDate birthdate;
    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_user",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"))
    @Getter(value = AccessLevel.NONE)
    private List<Book> books = new ArrayList<>();


    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setUsername(String username) {
        Preconditions.checkArgument(username.length() > 1, "The username field must have more than 1 character");
        this.username = checkNotNull(username);
    }

    public void setName(String name) {
        Preconditions.checkArgument(name.chars().allMatch(Character::isLetter),
                "The name field cannot have numbers");
        this.name = checkNotNull(name);
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkArgument(birthdate.isBefore(LocalDate.now()),
                "The birthday field must be less than or equal to the current year");
        this.birthdate = checkNotNull(birthdate);
    }

    public void setBooks(List<Book> books) {
        this.books = checkNotNull(books);
    }


    public void setPassword(String password) {
        this.password = password;
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
