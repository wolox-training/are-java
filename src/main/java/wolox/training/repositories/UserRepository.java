package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByName(String name);

    Optional<User> findFirstByUsername(String username);

    Page<User> findByBirthdateBetweenAndNameIgnoreCaseContaining(LocalDate start, LocalDate end,
            String string, Pageable pageable);

    @Query("SELECT u FROM USERS u WHERE (:dateFrom IS NULL OR u.birthdate >= :dateFrom) AND (:dateTo IS NULL"
            + " OR u.birthdate <= :dateTo) AND (:someChars IS NULL OR LOWER(u.name) LIKE CONCAT('%',LOWER(:someChars),'%'))")
    Page<User> findUserByBirthdayBetweenAndContaining(@Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("someChars") String someChars, Pageable pageable);
}
