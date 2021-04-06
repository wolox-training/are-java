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
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.Encoder;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.validators.BookValidator;
import wolox.training.validators.UserValidator;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
public class UserControllerTests {

    private User user1;
    private Book book1;
    private String userJsonString;
    private String bookJsonString;
    private String basicUrl;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private BookValidator bookValidator;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private Encoder encoder;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String password = "1234";
        basicUrl = "/api/users/";
        user1 = new User();
        user1.setUsername("Alis");
        user1.setName("Michella");
        user1.setBirthdate(LocalDate.of(1997, 5, 23));
        user1.setPassword(password);

        userJsonString = objectMapper.writeValueAsString(user1);
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
        Mockito.when(encoder.encode(any(String.class))).thenReturn(password);

    }


    @Test
    void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        String url = basicUrl + "1";
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userJsonString));
    }

    @Test
    void whenFindByIdWhichDoesNotExist_thenABadRequestIsReturned() throws Exception {
        doThrow(IdNotFoundException.class).when(userValidator).existsId(2L);
        String url = basicUrl + "2";
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateAnUserWithCorrectFields_thenItReturnsCreated() throws Exception {
        Mockito.when(userRepository.save(any())).thenReturn(user1);
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJsonString))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreateAnUserWithCorrectFieldsAnd1CharInUsernameField_thenItReturnsBadRequest() throws Exception {
        String jsonUser = "{"
                + "    \"username\": \"A\","
                + "    \"name\": \"Michella\","
                + "    \"birthdate\": \"23/05/1997\","
                + "    \"books\": []"
                + "}";
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateAnUserWithCorrectFieldsAndNumbersInTheNameField_thenItReturnsBadRequest() throws Exception {
        String jsonUser = "{"
                + "    \"username\": \"Alis\","
                + "    \"name\": \"1Michella\","
                + "    \"birthdate\": \"23/05/1997\","
                + "    \"books\": []"
                + "}";
        mvc.perform(post(basicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteAnUserWhichExists_thenItReturnsOk() throws Exception {
        doNothing().when(userRepository).deleteById(any(Long.class));
        Mockito.when(userValidator.existsId(any(Long.class))).thenReturn(user1);
        mvc.perform(delete(basicUrl + "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void whenDeleteAnUserWhichDoesNotExists_thenItReturnsBadRequest() throws Exception {
        doThrow(IdNotFoundException.class).when(userValidator).existsId(any(Long.class));
        mvc.perform(delete(basicUrl + "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateAnUserWhichExistsWithCorrectFields_thenItReturnsOk() throws Exception {
        Mockito.when(userValidator.idsMatchAndExist(any(User.class), any(Long.class))).thenReturn(user1);
        doNothing().when(userValidator).passwordMatch(any(User.class), any(User.class));

        mvc.perform(put(basicUrl + "1").contentType(MediaType.APPLICATION_JSON).content(userJsonString))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateAnUserWhichDoesNotExistsWithCorrectFields_thenItReturnsBadRequest() throws Exception {
        doThrow(IdNotFoundException.class).when(userValidator).idsMatchAndExist(any(User.class), any(Long.class));
        mvc.perform(put(basicUrl + "1").contentType(MediaType.APPLICATION_JSON).content(userJsonString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddsABookWhichExistsAndIsNotInTheUsersList_thenItReturnsOk() throws Exception {
        Mockito.when(userValidator.existsId(any(Long.class))).thenReturn(user1);
        Mockito.when(bookValidator.existsId(any(Long.class))).thenReturn(book1);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user1);
        mvc.perform(post(basicUrl + "1/books").contentType(MediaType.APPLICATION_JSON).content(bookJsonString))
                .andExpect(status().isOk());
    }

    @Test
    void whenAddsABookWhichExistsAndIsInTheUsersList_thenItReturnsBadRequest() throws Exception {
        user1.addBook(book1);
        Mockito.when(userValidator.existsId(any(Long.class))).thenReturn(user1);
        Mockito.when(bookValidator.existsId(any(Long.class))).thenReturn(book1);
        mvc.perform(post(basicUrl + "1/books").contentType(MediaType.APPLICATION_JSON).content(bookJsonString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRemovesABookWhichExistsAndIsInTheUsersList_thenItReturnsOk() throws Exception {
        user1.addBook(book1);
        Mockito.when(userValidator.existsId(any(Long.class))).thenReturn(user1);
        Mockito.when(bookValidator.existsId(any(Long.class))).thenReturn(book1);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user1);
        mvc.perform(delete(basicUrl + "1/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenRemovesABookWhichExistsAndIsNotInTheUsersList_thenItReturnsBadRequest() throws Exception {
        Mockito.when(userValidator.existsId(any(Long.class))).thenReturn(user1);
        Mockito.when(bookValidator.existsId(any(Long.class))).thenReturn(book1);
        mvc.perform(delete(basicUrl + "1/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}


