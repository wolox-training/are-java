package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Student;

public interface StudentRepository extends UserBaseRepository<Student>, CrudRepository<Student, Long> {

}
