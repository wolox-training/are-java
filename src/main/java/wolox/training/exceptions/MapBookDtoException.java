package wolox.training.exceptions;

public class MapBookDtoException extends RuntimeException {

    @Override
    public String getMessage() {
        return "The bookDto could not be mapped";
    }
}
