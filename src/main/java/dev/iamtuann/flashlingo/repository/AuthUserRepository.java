package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "authUserRepository")
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);

    AuthUser findAuthUserById(long id);

    Boolean existsByEmail(String email);
}
