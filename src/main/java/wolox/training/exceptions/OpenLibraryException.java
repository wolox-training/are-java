package wolox.training.exceptions;

public class OpenLibraryException extends RuntimeException {

    @Override
    public String getMessage() {
        return "The book could not be retrieved";
    }
}
