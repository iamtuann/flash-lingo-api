package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.AuthUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    AuthUserDto getUserById(long id);

    Page<AuthUserDto> searchUsers(String search, Integer status, Pageable pageable);
}
