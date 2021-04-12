package wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import wolox.training.dtos.BookDto;
import wolox.training.exceptions.MapBookDtoException;
import wolox.training.exceptions.OpenLibraryException;

public class OpenLibraryService {

    private final RestTemplate restTemplate = new RestTemplate();
    private String resourceName;
    private String url = "%s/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";
    public OpenLibraryService(String resourceName) {
        this.resourceName = resourceName;
    }

    public BookDto fromJsontoBookDto(String json, String isbn) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(json).path("ISBN:" + isbn);
            BookDto bookDto = objectMapper.treeToValue(jsonNode, BookDto.class);
            return bookDto;
        } catch (JsonProcessingException e) {
            throw new MapBookDtoException();
        }
    }

    public BookDto retrieveBook(Long isbn) {
        String isbnString = Strings.padStart(isbn.toString(), 10, '0');
        String urlWithIsbn = String.format(url, resourceName, isbnString);
        String response = restTemplate.getForObject(urlWithIsbn, String.class);
        if (response.equals("{}")) {
            throw new OpenLibraryException("The book was not found");
        }
        BookDto bookDto = this.fromJsontoBookDto(response, isbnString);
        bookDto.setIsbn(isbn);
        return bookDto;
    }

    public BookDto bookInfo(Long isbn) {
        try {
            return retrieveBook(isbn);
        } catch (RestClientException e) {
            throw new OpenLibraryException("Open library is down");
        }
    }

}
