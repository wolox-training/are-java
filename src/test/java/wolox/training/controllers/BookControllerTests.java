package wolox.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.exceptions.NullFieldException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.BookService;
import wolox.training.validators.BookValidator;

@WebMvcTest(controllers = BookController.class)
@ActiveProfiles("test")
public class BookControllerTests {

    private static Book book1;
    private static String bookWithVariableYearJsonString;
    private static String bookJsonString;
    private static String basicUrl;
    private static ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookValidator bookValidator;
    @MockBean
    private BookService bookService;


    @BeforeAll
    static void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        basicUrl = "/api/books/";

        bookWithVariableYearJsonString = "{ "
                + "  \""
                + "genre\": \"Fantasy\","
                + "  \"author\":\"J. K. Rowling\","
                + "  \"image\":\"image.jpg\","
                + "  \"title\":\"Harry Potter and the Philosopher's Stone\","
                + "  \"subtitle\":\"-\","
                + "  \"publisher\":\"Bloomsbury Publishing\","
                + "  \"year\": %s,"
                + "  \"pages\":223,  "
                + "  \"isbn\":9780747532743"
                + "  }";

        book1 = new Book();
        book1.setGenre("Fantasy");
        book1.setAuthor("J. K. Rowling");
        book1.setImage("image.jpg");
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setSubtitle("-");
        book1.setPublisher("Bloomsbury Publishing");
        book1.setYear("1997");
        book1.setPages(223);
        book1.setIsbn("9780747532743");
        bookJsonString = objectMapper.writeValueAsString(book1);


    }


    @Test
    void whenFindByIdWhichExists_thenBookIsReturned() throws Exception {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        String url = basicUrl + "1";
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(bookJsonString));
    }

    @Test
    void whenFindByIdWhichDoesNotExist_thenABadRequestIsReturned() throws Exception {
        doThrow(IdNotFoundException.class).when(bookValidator).existsId(2L);
        String url = basicUrl + "2";
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateABookWithCorrectFields_thenItReturnsCreated() throws Exception {

        Mockito.when(bookRepository.save(any())).thenReturn(book1);
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJsonString))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreateABookWithNullFieldsWhenItShouldNotBeNull_thenItReturnsBadRequest() throws Exception {
        String jsonBook = "{ "
                + "  \"genre\": \"Fantasy\","
                + "  \"image\":\"image.jpg\","
                + "  \"title\":\"Harry Potter and the Philosopher's Stone\","
                + "  \"subtitle\":\"-\","
                + "  \"publisher\":\"Bloomsbury Publishing\","
                + "  \"year\":1997,"
                + "  \"pages\":223,  "
                + "  \"isbn\":9780747532743"
                + "  }";
        doThrow(NullFieldException.class).when(bookValidator).validateFields(any(Book.class));
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateABookWithCorrectFieldsAnd1CharInGenreField_thenItReturnsBadRequest() throws Exception {
        String jsonBook = "{ "
                + "  \"genre\": \"F\","
                + "  \"author\":\"J. K. Rowling\","
                + "  \"image\":\"image.jpg\","
                + "  \"title\":\"Harry Potter and the Philosopher's Stone\","
                + "  \"subtitle\":\"-\","
                + "  \"publisher\":\"Bloomsbury Publishing\","
                + "  \"year\":1997,"
                + "  \"pages\":223,  "
                + "  \"isbn\":9780747532743"
                + "  }";
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateABookWithCorrectFieldsAndEmptyFieldAuthor_thenItReturnsBadRequest() throws Exception {
        String jsonBook = "{ "
                + "  \"genre\": \"Fantasy\","
                + "  \"author\":\"\","
                + "  \"image\":\"image.jpg\","
                + "  \"title\":\"Harry Potter and the Philosopher's Stone\","
                + "  \"subtitle\":\"-\","
                + "  \"publisher\":\"Bloomsbury Publishing\","
                + "  \"year\":1997,"
                + "  \"pages\":223,  "
                + "  \"isbn\":9780747532743"
                + "  }";
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateABookWithGreaterYearThatTheCurrentOneAndTheOtherFieldsCorrects_thenItReturnsBadRequest()
            throws Exception {
        int year = LocalDateTime.now().getYear() + 1;
        String jsonBook = String.format(bookWithVariableYearJsonString, year);
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateABookWithTheCurrentYearAndTheOtherFieldsCorrects_thenItReturnsCreated() throws Exception {
        int year = LocalDateTime.now().getYear();
        String jsonBook = String.format(bookWithVariableYearJsonString, year);
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreateABookWithALowerYearThatTheCurrentOneAndTheOtherFieldsCorrects_thenItReturnsCreated()
            throws Exception {
        int year = LocalDateTime.now().getYear() - 12;
        String jsonBook = String.format(bookWithVariableYearJsonString, year);
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andExpect(status().isCreated());
    }

    @Test
    void whenDeleteABookWhichExists_thenItReturnsOk() throws Exception {
        doNothing().when(bookRepository).deleteById(any());
        Mockito.when(bookValidator.existsId(any(Long.class))).thenReturn(book1);
        mvc.perform(delete(basicUrl + "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void whenDeleteABookWhichDoesNotExists_thenItReturnsBadRequest() throws Exception {
        doThrow(IdNotFoundException.class).when(bookValidator).existsId(any());
        mvc.perform(delete(basicUrl + "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateABookWhichExistsWithCorrectFields_thenItReturnsOk() throws Exception {
        Mockito.when(bookValidator.idsMatchAndExist(any(Book.class), any(Long.class))).thenReturn(book1);
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(book1);
        mvc.perform(put(basicUrl + "1").contentType(MediaType.APPLICATION_JSON).content(bookJsonString))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateABookWhichDoesNotExistsWithCorrectFields_thenItReturnsBadRequest() throws Exception {
        doThrow(IdNotFoundException.class).when(bookValidator).idsMatchAndExist(any(Book.class), any(Long.class));
        mvc.perform(put(basicUrl + "1").contentType(MediaType.APPLICATION_JSON).content(bookJsonString))
                .andExpect(status().isBadRequest());
    }

}
