package wolox.training.exceptions;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException() {
        super("The id does not exist");
    }
}
