package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.exception.ResourceNotFoundException;
import dev.iamtuann.flashlingo.mapper.AuthUserMapper;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import dev.iamtuann.flashlingo.repository.UserRepository;
import dev.iamtuann.flashlingo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthUserMapper authUserMapper = AuthUserMapper.INSTANCE;

    @Override
    public AuthUserDto getUserById(long id) {
        AuthUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return authUserMapper.toDto(user);
    }

    @Override
    public Page<AuthUserDto> searchUsers(String search, Integer status, Pageable pageable) {
        Page<AuthUser> authUserPage = userRepository.searchUsers(search, status, pageable);
        List<AuthUserDto> users = authUserPage.stream()
                .map(authUserMapper::toDto).toList();
        return new PageImpl<>(users, pageable, authUserPage.getTotalElements());
    }
}
