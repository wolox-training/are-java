package wolox.training.exceptions;

public class PasswordChangeNotAllowed extends RuntimeException {

    public PasswordChangeNotAllowed(String message) {
        super(message);
    }
}
