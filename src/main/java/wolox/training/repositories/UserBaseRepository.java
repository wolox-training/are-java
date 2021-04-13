package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
@NoRepositoryBean
public interface UserBaseRepository<T>
        extends Repository<T, Long> {


    Page<T> findAll(Pageable pageable);

    @Query("SELECT u FROM #{#entityName} u WHERE (?1 IS NULL OR u.birthdate >= ?1) AND (?2 IS NULL"
            + " OR u.birthdate <= ?2) AND (?3 IS NULL OR LOWER(u.name) LIKE CONCAT('%',LOWER(?3),'%'))")
    Page<T> findUserByBirthdayBetweenAndContaining(@Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("someChars") String someChars, Pageable pageable);

    Optional<T> findFirstByName(String name);

    Optional<T> findFirstByUsername(String username);

    Page<T> findByBirthdateBetweenAndNameIgnoreCaseContaining(LocalDate start, LocalDate end,
            String string, Pageable pageable);



}


