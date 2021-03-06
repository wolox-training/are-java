package wolox.training.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.validators.BookValidator;
import wolox.training.validators.UserValidator;

@RequestMapping("/api/users")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private PasswordEncoder encoder;


    /**
     * This method searches a user
     *
     * @param id: Identifies the user to be searched. (Long)
     *
     * @return the result of the search.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);
    }

    /**
     * This method creates a user
     *
     * @param user: The new user to be saved (User)
     *
     * @return the saved user
     */
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        userValidator.validateFields(user);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * This method removes a user
     *
     * @param id: Identifies the user who will be removed (Long)
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        userValidator.existsId(id);
        userRepository.deleteById(id);
    }

    /**
     * This method updates a user
     *
     * @param user: The user who will be modified (User)
     * @param id:   Identifies the user (Long)
     *
     * @return the user updated
     */
    @PutMapping("/{id}")
    @ResponseBody
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        User userFromRepo = userValidator.idsMatchAndExist(user, id);
        userValidator.passwordMatch(user, userFromRepo);
        return userRepository.save(user);
    }

    /**
     * This method adds a book to the user's book list
     *
     * @param userId: Identifies the user who will add a book to their list (Long)
     * @param book:   The book who will be added (Book)
     *
     * @return the user with the book added to their list
     */
    @PostMapping("/{userId}/books")
    @ResponseBody
    public User addBook(@RequestBody Book book, @PathVariable Long userId) {
        User user = userValidator.existsId(userId);
        Book book1 = bookValidator.existsId(book.getId());
        user.addBook(book1);
        return userRepository.save(user);
    }

    /**
     * This method removes a book from the user's book list
     *
     * @param userId: Identifies the user who will remove a book from their list (Long)
     * @param bookId  Identifies the book who will be removed (Long)
     *
     * @return the user without their book on the list
     */
    @DeleteMapping("/{userId}/books/{bookId}")
    @ResponseBody
    public User removeBook(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userValidator.existsId(userId);
        Book book = bookValidator.existsId(bookId);
        user.removeBook(book);
        return userRepository.save(user);
    }

    @GetMapping
    @ResponseBody
    public List<User> list() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/username")
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }


}
