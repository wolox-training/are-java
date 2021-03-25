package wolox.training.exceptions;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("The book id does not exist");
    }
}
