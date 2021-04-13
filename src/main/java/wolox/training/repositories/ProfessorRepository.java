package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Professor;

public interface ProfessorRepository extends UserBaseRepository<Professor>, CrudRepository<Professor, Long> {

}
