package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.AuthUserDto;
import dev.iamtuann.flashlingo.model.PageDto;
import org.springframework.data.domain.Pageable;

public interface UserService {
    AuthUserDto getUserById(long id);

    PageDto<AuthUserDto> searchUsers(String search, Integer status, Pageable pageable);

    PageDto<AuthUserDto> getTopCreators(Pageable pageable);
}
