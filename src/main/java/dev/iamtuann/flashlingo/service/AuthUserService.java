package dev.iamtuann.flashlingo.service;


import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.model.request.LoginDto;
import dev.iamtuann.flashlingo.model.request.RegisterDto;
import dev.iamtuann.flashlingo.model.request.UserRequest;

public interface AuthUserService {
    AuthUserResponse login(LoginDto loginDto);

    AuthUser register(RegisterDto registerDto);

    AuthUserDto getUserById(Long id);

    AuthUserDto updateUserById(UserRequest request, Long userId);

    void changePassword(Long userId, String currentPassword, String newPassword);
}
