package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
