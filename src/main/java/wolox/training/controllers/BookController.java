package wolox.training.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.BookService;
import wolox.training.validators.BookValidator;

@RequestMapping("/api/books")
@RestController

public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private BookService bookService;

    /**
     * This method searches a book
     *
     * @param id: Identifies the book to be searched (Book)
     *
     * @return the result of the search.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Given and id, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "The id does not exist"),
            @ApiResponse(code = 200, message = "")
    })
    @ResponseBody
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);
    }

    /**
     * This method create a book
     *
     * @param book: The book to be created (Book)
     *
     * @return the saved book.
     */
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        bookValidator.validateFields(book);
        return bookRepository.save(book);
    }

    /**
     * This method removes a book
     *
     * @param id: Identifies the book to be removed (Long)
     */
    @DeleteMapping("/{id}")
    @ResponseBody

    public void delete(@PathVariable Long id) {
        bookValidator.existsId(id);
        bookRepository.deleteById(id);
    }

    /**
     * This method updates a book
     *
     * @param book: The book to be updated (Book)
     * @param id:   Identifies the book (Long)
     *
     * @return the updated book.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        bookValidator.idsMatchAndExist(book, id);
        return bookRepository.save(book);
    }

    /**
     * This method says hello
     *
     * @param name:  The optional name to say hello to (String)
     * @param model: Contains the data that appears in the view (Model)
     *
     * @return the view saying hello to the name or its default.
     */
    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> list(
            @RequestParam(name = "isbn", required = false) Long isbn,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "image", required = false) String image,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "subtitle", required = false) String subtitle,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "pages", required = false) Integer pages) {

        return new ResponseEntity<>(
                this.bookRepository.findBooksBy(isbn, genre, author, image, title, subtitle, publisher, year, pages),
                HttpStatus.OK);
    }

    @GetMapping("/isbn/{isbnNumber}")
    private ResponseEntity<Object> getBookByIsbn(@PathVariable Long isbnNumber) {
        Optional<Book> bookOptional = bookRepository.findFirstByIsbn(isbnNumber);
        if (bookOptional.isPresent()) {
            return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
        } else {
            Book book = bookService.searchBook(isbnNumber);
            bookRepository.save(book);
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        }
    }
}

