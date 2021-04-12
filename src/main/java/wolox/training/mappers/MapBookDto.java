package wolox.training.mappers;

import java.util.List;
import java.util.stream.Collectors;
import wolox.training.dtos.BookDto;
import wolox.training.models.Book;


public class MapBookDto {

    private String nameListMerged(List<String> nameList) {
        if (nameList.size() > 1) {
            String lastName = nameList.get(nameList.size() - 1);
            nameList.remove(lastName);
            StringBuilder names = new StringBuilder(String.join(",", nameList));
            names.append("and " + lastName);
            return names.toString();
        } else {
            return nameList.get(0);
        }

    }

    public Book mapToBook(BookDto bookDto) {
        Book book = new Book();
        book.setAuthor(
                this.nameListMerged(bookDto.getAuthors().stream()
                        .map(authorDto -> authorDto.getName()).collect(
                                Collectors.toList())));
        book.setPublisher(this.nameListMerged(
                bookDto.getPublishers().stream().map(publisherDto -> publisherDto.getName())
                        .collect(Collectors.toList())));
        book.setTitle(bookDto.getTitle());
        book.setSubtitle(bookDto.getSubtitle());
        book.setYear(bookDto.getPublishDate());
        book.setPages(bookDto.getNumberOfPages());
        book.setIsbn(bookDto.getIsbn());
        return book;

    }
}
