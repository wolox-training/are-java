package wolox.training.validators;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.IdNotFoundException;
import wolox.training.exceptions.NullFieldException;
import wolox.training.exceptions.PasswordChangeNotAllowed;
import wolox.training.exceptions.UsernameExistsException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;
import wolox.training.security.PasswordEncoder;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User existsId(Long id) {
        return this.userRepository.findById(id).orElseThrow(IdNotFoundException::new);
    }

    public User idsMatchAndExist(User user, Long id) {
        if (user.getId() != id) {
            throw new IdMismatchException();
        }
        return this.existsId(id);
    }

    public void validateFields(User user) {
        try {
            Preconditions.checkNotNull(user.getBirthdate(), "Birthday must have a value");
            Preconditions.checkNotNull(user.getName(), "Name must have a value");
            Preconditions.checkNotNull(user.getUsername(), "Username must have a value");
            Preconditions.checkArgument(!userRepository.findFirstByUsername(user.getUsername()).isPresent(),
                    "The username has already been taken");
        } catch (NullPointerException e) {
            throw new NullFieldException(e);
        } catch (IllegalArgumentException e) {
            throw new UsernameExistsException(e);
        }
    }

    public void passwordMatch(User userFromRequest, User userFromRepo) {
        if (!passwordEncoder.encoder().matches(userFromRequest.getPassword(), userFromRepo.getPassword())) {
            throw new PasswordChangeNotAllowed("The password cannot be change when the user is updated");
        }

    }
}
