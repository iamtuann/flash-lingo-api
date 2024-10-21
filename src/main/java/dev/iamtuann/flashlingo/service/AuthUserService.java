package dev.iamtuann.flashlingo.service;


import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.model.request.LoginDto;
import dev.iamtuann.flashlingo.model.request.RegisterDto;

public interface AuthUserService {
    AuthUserResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
