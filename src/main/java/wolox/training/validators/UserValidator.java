package wolox.training.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.repositories.UserRepository;

@Component
public class UserValidator {

  @Autowired
  private UserRepository userRepository;

  public void userExist(Long id){
    this.userRepository.findById(id).orElseThrow(IdNotFoundException::new);
  }

}
