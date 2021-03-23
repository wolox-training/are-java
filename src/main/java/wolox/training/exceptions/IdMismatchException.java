package wolox.training.exceptions;

public class BookIdMismatchException extends RuntimeException {

  public BookIdMismatchException() {
    super("The id indicated in the book and the id provided does not match");
  }
}
