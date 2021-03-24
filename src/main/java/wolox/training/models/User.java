package wolox.training.models;

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
import javax.persistence.OneToMany;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNeverOwnedException;

@Entity(name = "USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(nullable = false)
  String username;
  @Column(nullable = false)
  String name;
  @Column(nullable = false)
  LocalDate birthdate;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "book_user",
      joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id",
          referencedColumnName = "id"))
  List<Book> books = new ArrayList<>();

  public User() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(books);
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }

  public void addBook(Book book){
    if(this.books.stream().anyMatch(book1 -> book1.getId()==book.getId()))
      throw new BookAlreadyOwnedException();
    this.books.add(book);
  }
  public void removeBook(Book book){
    if(!this.books.remove(book));
    throw new BookNeverOwnedException();
  }
}