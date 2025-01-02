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
            "WHERE (:search IS NULL OR :name = '' OR a.firstName LIKE CONCAT('%', :search, '%') " +
            "OR a.lastName LIKE CONCAT('%', :search, '%') " +
            "OR a.email LIKE CONCAT('%', :search, '%') " +
            "OR CONCAT(a.firstName, a.lastName) LIKE CONCAT('%', :search, '%')) " +
            "AND (:status IS NULL OR a.status = :status)" )
    Page<AuthUser> searchUsers(@Param("search") String search,
                             @Param("status") Integer status,
                             Pageable pageable);
}
