package wolox.training.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findByName(String name);

}
