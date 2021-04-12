package wolox.training.services;

import org.springframework.stereotype.Service;
import wolox.training.dtos.BookDto;
import wolox.training.mappers.MapBookDto;
import wolox.training.models.Book;

@Service
public class BookService {

    private final MapBookDto mapBookDto = new MapBookDto();
    private String resourceName = "https://openlibrary.org";

    public Book searchBook(Long isbn) {
        OpenLibraryService openLibraryService = new OpenLibraryService(resourceName);
        BookDto bookDto = openLibraryService.bookInfo(isbn);
        return this.mapBookDto.mapToBook(bookDto);
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
