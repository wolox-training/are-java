package wolox.training.exceptions;

public class IdMismatchException extends RuntimeException {

    public IdMismatchException() {
        super("The id indicated in the path and the id provided in the body does not match");
    }
}
