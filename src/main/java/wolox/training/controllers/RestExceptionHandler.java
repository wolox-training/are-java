package wolox.training.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNeverOwnedException;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.exceptions.NullFieldException;
import wolox.training.exceptions.OpenLibraryException;
import wolox.training.exceptions.PasswordChangeNotAllowed;
import wolox.training.exceptions.UsernameExistsException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IdMismatchException.class, IdNotFoundException.class, PasswordChangeNotAllowed.class})
    public ResponseEntity<Object> handleBookIdMismatch(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({BookAlreadyOwnedException.class, BookNeverOwnedException.class, NullFieldException.class,
            IllegalStateException.class,
            UsernameExistsException.class})
    public ResponseEntity<Object> handleTheUserAlreadyHasThatBook(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(OpenLibraryException.class)
    public ResponseEntity<Object> handleNotFoundBook(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
