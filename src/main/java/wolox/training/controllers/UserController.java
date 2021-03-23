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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Controller
@RequestMapping(path = "/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserRepository bookRepository;

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
    userRepository.findById(id)
        .orElseThrow(IdNotFoundException::new);
    userRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ResponseBody
  public User updateUser(@RequestBody User User, @PathVariable Long id) {
    if (User.getId() != id) {
      throw new IdMismatchException();
    }
    userRepository.findById(id)
        .orElseThrow(IdNotFoundException::new);
    return userRepository.save(User);
  }

/*  @PatchMapping("/{id}/books/{idBook}")
  @ResponseBody
  public User addBook(@RequestBody Book book, @PathVariable Long id,@PathVariable Long idBook) {
    if (book.getId() != id) {
      throw new IdMismatchException();
    }
    bookRepository.findById(idBook).orElseThrow(I)
    userRepository.findById(id)
        .orElseThrow(IdNotFoundException::new).addBook(book);
    return userRepository.save(user);
  }
*/

}
