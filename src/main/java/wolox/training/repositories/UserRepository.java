package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByName(String name);

    Optional<User> findFirstByUsername(String username);

    Optional<List<User>> findByBirthdateBetweenAndNameIgnoreCaseContaining(LocalDate start, LocalDate end,
            String string);

    @Query("SELECT u FROM USERS u WHERE (:dateFrom IS NULL OR u.birthdate >= :dateFrom) AND (:dateTo IS NULL"
            + " OR u.birthdate <= :dateTo) AND (:someChars IS NULL OR LOWER(u.name) LIKE CONCAT('%',LOWER(:someChars),'%'))")
    Optional<List<User>> findUserByBirthdayBetweenAndContaining(@Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("someChars") String someChars);
}
