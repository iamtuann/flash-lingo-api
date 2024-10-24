package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.Role;
import dev.iamtuann.flashlingo.enums.ERole;
import dev.iamtuann.flashlingo.exception.APIException;
import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.model.request.LoginDto;
import dev.iamtuann.flashlingo.model.request.RegisterDto;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.RoleRepository;
import dev.iamtuann.flashlingo.security.JwtAuthenticationFilter;
import dev.iamtuann.flashlingo.security.JwtTokenProvider;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.impl.AuthUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(MockitoExtension.class)
public class AuthUserServiceTest {
    @Mock
    private AuthUserRepository authUserRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private Authentication authentication;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AuthUserServiceImpl authUserService;

    private LoginDto mockLoginDto() {
        return new LoginDto("test@gmail.com", "123456");
    }

    private RegisterDto mockRegisterDto() {
        return new RegisterDto("test@gmail.com", "123456", "first_name", "last_name");
    }

    private AuthUser mockAuthUser() {
        Role role = new Role(3L, "USER", "User");
        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setFirstName("first_name");
        user.setLastName("last_name");
        user.setPassword("123456");
        user.setProvider("System");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        return user;
    }

    @Test
    void login_shouldReturnAuthUserResponse() {
        LoginDto loginDto = mockLoginDto();
        AuthUser user = mockAuthUser();
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        when(authUserRepository.existsByEmail(loginDto.getEmail())).thenReturn(true);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        )).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("fake-jwt-token");

        AuthUserResponse response = authUserService.login(loginDto);
        assertNotNull(response);
        assertEquals(userDetails.getId(), response.getId());
        assertEquals(userDetails.getUsername(), response.getEmail());
        assertEquals(userDetails.getFirstName(), response.getFirstName());
        assertEquals(userDetails.getLastName(), response.getLastName());
        assertEquals("fake-jwt-token", response.getToken());
    }

    @Test
    public void login_shouldThrowException_WhenEmailNotFound() {
        LoginDto loginDto = mockLoginDto();
        when(authUserRepository.existsByEmail(loginDto.getEmail())).thenReturn(false);
        assertThrows(APIException.class, ()-> authUserService.login(loginDto));
    }

    @Test
    void login_shouldThrowException_WhenInvalidPassword() {
        LoginDto loginDto = new LoginDto("test@gmail.com", "wrongPassword");
        when(authUserRepository.existsByEmail(loginDto.getEmail())).thenReturn(true);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        )).thenThrow(new BadCredentialsException("Bad credentials"));
        assertThrows(APIException.class, () -> authUserService.login(loginDto));
    }

    @Test
    void register_shouldThrowException_WhenEmailAlreadyExist() {
        RegisterDto registerDto = mockRegisterDto();
        when(authUserRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);
        assertThrows(APIException.class, ()-> authUserService.register(registerDto));
    }

    @Test
    void register_shouldReturnAuthUser() {
        RegisterDto registerDto = mockRegisterDto();
        AuthUser user = mockAuthUser();
        Role role = new Role(3L, "USER", "User");
        when(authUserRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("123456");
        when(roleRepository.findByName(ERole.USER.getName())).thenReturn(role);
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(user);
        AuthUser response = authUserService.register(registerDto);

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getPassword(), response.getPassword());
    }
}
