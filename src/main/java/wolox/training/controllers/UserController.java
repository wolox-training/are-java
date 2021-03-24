package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.validators.BookValidator;
import wolox.training.validators.UserValidator;

@Controller
@RequestMapping(path = "/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private UserValidator userValidator;

  @Autowired
  private BookValidator bookValidator;

  @GetMapping("/{id}")
  @ResponseBody
  public User findOne(@PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(IdNotFoundException::new);
  }

  @PostMapping
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody User User) {
    return userRepository.save(User);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public void delete(@PathVariable Long id) {
    userValidator.existsId(id);
    userRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ResponseBody
  public User updateUser(@RequestBody User user, @PathVariable Long id) {
    userValidator.idsMatchAndExist(user,id);
    return userRepository.save(user);
  }

  @PostMapping("/{userId}/books")
  @ResponseBody
  public User addBook(@RequestBody Book book, @PathVariable Long userId) {
    userValidator.existsId(userId);
    bookValidator.existsId(book.getId());
    User user=userRepository.findById(userId).get();
    user.addBook(book);
    return userRepository.save(user);
  }

  @DeleteMapping("/{userId}/books/{bookId}")
  @ResponseBody
  public User removeBook(@PathVariable Long userId,@PathVariable Long bookId) {
    bookValidator.existsId(bookId);
    userValidator.existsId(userId);
    User user=userRepository.findById(userId).get();
    Book book= bookRepository.findById(bookId).get();
    user.removeBook(book);
    return userRepository.save(user);
  }

}
