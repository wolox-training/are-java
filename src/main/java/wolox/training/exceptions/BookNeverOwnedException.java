package wolox.training.exceptions;

public class BookNeverOwnedException extends RuntimeException{
  public BookNeverOwnedException() {
    super("The user has not that book hence it can not be removed from the book list");
  }
}
