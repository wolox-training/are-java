package wolox.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.training.dtos.BookDto;
import wolox.training.mappers.MapBookDto;
import wolox.training.models.Book;

@Service
public class BookService {

    private final MapBookDto mapBookDto = new MapBookDto();
    @Autowired
    private OpenLibraryService openLibraryService;

    public Book searchBook(String isbn) {
        BookDto bookDto = openLibraryService.bookInfo(isbn);
        return this.mapBookDto.mapToBook(bookDto);
    }

}
