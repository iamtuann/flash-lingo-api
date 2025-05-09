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
import dev.iamtuann.flashlingo.model.request.UserRequest;
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

import java.util.*;

@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserMapper authUserMapper = AuthUserMapper.INSTANCE;
    private final Random random = new Random();

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
        user.setUsername(generateUsername());
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

    @Override
    public AuthUserDto updateUserById(UserRequest request, Long userId) {
        AuthUser authUser = authUserRepository.findAuthUserById(userId);
        if (!Objects.equals(authUser.getUsername(), request.getUsername()) &&  authUserRepository.existsByUsername(request.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }

        authUser.setUsername(request.getUsername());
        authUser.setFirstName(request.getFirstName());
        authUser.setLastName(request.getLastName());
        authUser.setDob(request.getDob());
        authUser.setBio(request.getBio());
        authUser.setUpdatedAt(new Date());
        authUserRepository.save(authUser);
        return authUserMapper.toDto(authUser);
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        AuthUser authUser = authUserRepository.findAuthUserById(userId);
        boolean isMatches = passwordEncoder.matches(currentPassword, authUser.getPassword());
        if (isMatches) {
            authUser.setPassword(passwordEncoder.encode(newPassword));
            authUserRepository.save(authUser);
        } else {
            throw new APIException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
    }

    public String generateUsername() {
        String username;
        do {
            username = "User" + String.format("%08d", random.nextInt(100_000_000));
        } while (authUserRepository.existsByUsername(username));
        return username;
    }
}
