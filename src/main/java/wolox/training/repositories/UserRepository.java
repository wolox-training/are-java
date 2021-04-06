package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByName(String name);

    Optional<User> findFirstByUsername(String username);

    Optional<List<User>> findByBirthdateBetweenAndNameIgnoreCaseContaining(LocalDate start, LocalDate end,
            String string);
}
