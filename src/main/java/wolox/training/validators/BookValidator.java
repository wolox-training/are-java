package wolox.training.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.repositories.BookRepository;

@Component
public class BookValidator {
  @Autowired
  BookRepository bookRepository;

  public void bookExist(Long id){
    bookRepository.findById(id).orElseThrow(IdNotFoundException::new);
  }


}
