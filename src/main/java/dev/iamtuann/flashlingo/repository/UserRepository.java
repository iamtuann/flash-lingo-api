package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "userRepository")
public interface UserRepository extends JpaRepository<AuthUser, Long> {

    @Query(value = "SELECT a FROM AuthUser a " +
            "WHERE (:search IS NULL OR :search = '' " +
            "OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "AND (:status IS NULL OR a.status = :status))" )
    Page<AuthUser> searchUsers(@Param("search") String search,
                             @Param("status") Integer status,
                             Pageable pageable);

    @Query("SELECT au FROM AuthUser au LEFT JOIN au.topics t " +
            "WHERE t.status = 1 " +
            "GROUP BY au.id " +
            "ORDER BY COUNT(t) DESC")
    Page<AuthUser> getTopCreators(Pageable pageable);
}
