package wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import wolox.training.dtos.BookDto;
import wolox.training.exceptions.MapBookDtoException;
import wolox.training.exceptions.OpenLibraryException;

@Service
public class OpenLibraryService {

    private static String url = "https://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";
    private final RestTemplate restTemplate = new RestTemplate();

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

    public BookDto bookInfo(Long isbn) {
        String isbnString=Strings.padStart(isbn.toString(),10,'0');
        String urlWithIsbn = String.format(url, isbnString);
        String response = restTemplate.getForObject(urlWithIsbn, String.class);
        if (response.equals("{}") || response == null) {
            throw new OpenLibraryException();
        }
        BookDto bookDto = this.fromJsontoBookDto(response, isbnString);
        bookDto.setIsbn(isbn);
        return bookDto;
    }

}
