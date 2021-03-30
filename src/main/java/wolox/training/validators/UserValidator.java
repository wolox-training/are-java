package wolox.training.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public void existsId(Long id) {
        this.userRepository.findById(id).orElseThrow(IdNotFoundException::new);
    }

    public void idsMatchAndExist(User user, Long id) {
        if (user.getId() != id) {
            throw new IdMismatchException();
        }
        this.existsId(id);
    }
}
