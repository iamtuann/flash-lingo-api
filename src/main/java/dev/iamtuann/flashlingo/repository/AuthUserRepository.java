package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findById(long id);

    Boolean existsByEmail(String email);
}
