package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.validators.BookValidator;

@Controller
@RequestMapping(path = "/books")

public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BookValidator bookValidator;

  @GetMapping("/{id}")
  @ResponseBody
  public Book findOne(@PathVariable Long id) {
    bookValidator.existsId(id);
    return bookRepository.findById(id).get();
  }

  @PostMapping
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public void delete(@PathVariable Long id) {
    bookValidator.existsId(id);
    bookRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ResponseBody
  public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
    bookValidator.idsMatchAndExist(book,id);
    return bookRepository.save(book);
  }

  @GetMapping("/greeting")
  public String greeting(
      @RequestParam(name = "name", required = false, defaultValue = "World") String name,
      Model model) {
    model.addAttribute("name", name);
    return "greeting";
  }


}
