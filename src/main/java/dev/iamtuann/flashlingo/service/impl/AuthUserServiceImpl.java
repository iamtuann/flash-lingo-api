package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.Role;
import dev.iamtuann.flashlingo.enums.ERole;
import dev.iamtuann.flashlingo.exception.APIException;
import dev.iamtuann.flashlingo.mapper.AuthUserMapper;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.model.request.LoginDto;
import dev.iamtuann.flashlingo.model.request.RegisterDto;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.RoleRepository;
import dev.iamtuann.flashlingo.security.JwtTokenProvider;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.AuthUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserMapper authUserMapper = AuthUserMapper.INSTANCE;

    @Override
    public AuthUserResponse login(LoginDto loginDto) {
        if (!authUserRepository.existsByEmail(loginDto.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is not registered");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return new AuthUserResponse((UserDetailsImpl) authentication.getPrincipal(), token);
        } catch (BadCredentialsException ex) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    @Override
    public AuthUser register(RegisterDto registerDto) {
        if (authUserRepository.existsByEmail(registerDto.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is already exists!");
        }
        AuthUser user = new AuthUser();
        user.setEmail(registerDto.getEmail());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setProvider("System");
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.USER.getName()));
        user.setRoles(roles);
        return authUserRepository.save(user);
    }

    @Override
    public AuthUserDto getUserById(Long id) {
        AuthUser authUser = authUserRepository.findAuthUserById(id);
        return authUserMapper.toDto(authUser);
    }
}
