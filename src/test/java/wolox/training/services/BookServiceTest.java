package wolox.training.services;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.exceptions.OpenLibraryException;
import wolox.training.models.Book;
import wolox.training.services.UtilsForTest.OpenLibraryResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    private OpenLibraryResponse openLibraryResponse = new OpenLibraryResponse();

    @Rule
    WireMockRule wireMockRule = new WireMockRule(options().port(8888));

    @BeforeEach
    void setUp() {
        wireMockRule.start();
        bookService.setResourceName("http://localhost:" + wireMockRule.port());
    }

    @AfterEach
    void after() {
        wireMockRule.stop();
    }

    private Map getBookMap() {
        Map bookMap = new HashMap();
        bookMap.put("Author", "J. K. Rowling");
        bookMap.put("Title", "Harry Potter and the Philosopher's Stone");
        bookMap.put("Subtitle", "-");
        bookMap.put("Publisher", "Bloomsbury Publishing");
        bookMap.put("Year", "1997");
        bookMap.put("Pages", 223);
        bookMap.put("Isbn", 121212L);
        return bookMap;
    }

    @Test
    void whenSearchForABookInOpenLibraryByIsbnAndItExists_thenItIsMappedCorrectly() {
        Map bookMap = this.getBookMap();
        wireMockRule.stubFor(any(anyUrl()).willReturn(okJson(openLibraryResponse.whenItFoundsTheBook(bookMap))));
        Book book = bookService.searchBook((Long) bookMap.get("Isbn"));
        assertEquals(book.getAuthor(), bookMap.get("Author"));
        assertEquals(bookMap.get("Title"), book.getTitle());
        assertEquals(bookMap.get("Subtitle"), book.getSubtitle());
        assertEquals(bookMap.get("Publisher"), book.getPublisher());
        assertEquals(bookMap.get("Year"), book.getYear());
        assertEquals(bookMap.get("Pages"), book.getPages());
        assertEquals(bookMap.get("Isbn"), book.getIsbn());
    }

    @Test
    void whenSearchForABookInOpenLibraryByIsbnAndItDoesNotExists_thenACustomExceptionIsThrown() {
        wireMockRule.stubFor(any(anyUrl()).willReturn(okJson(openLibraryResponse.whenItDoesNotFoundTheBook())));
        Exception exception = assertThrows(OpenLibraryException.class, () -> bookService.searchBook(3232L));
        assertSame("The book was not found", exception.getMessage());
    }
    @Test
    void whenSearchForABookInOpenLibraryAndItIsDown_thenACustomExceptionIsThrown() {
        wireMockRule.stubFor(any(anyUrl()).willReturn(serverError()));
        Exception exception = assertThrows(OpenLibraryException.class, () -> bookService.searchBook(3232L));
        assertSame("Open library is down", exception.getMessage());
    }
}
