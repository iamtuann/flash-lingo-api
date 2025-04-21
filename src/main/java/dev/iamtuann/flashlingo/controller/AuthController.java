package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.model.request.ChangePasswordRequest;
import dev.iamtuann.flashlingo.model.request.LoginDto;
import dev.iamtuann.flashlingo.model.request.RegisterDto;
import dev.iamtuann.flashlingo.model.request.UserRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthUserService authUserService;

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthUserResponse> login(@Valid @RequestBody LoginDto loginDto) {
        AuthUserResponse userResponse = authUserService.login(loginDto);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        AuthUser user = authUserService.register(registerDto);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<AuthUserDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        AuthUserDto userDto = authUserService.getUserById(userDetails.getId());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<AuthUserDto> updateProfile(
            @RequestBody @Valid UserRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AuthUserDto userDto = authUserService.updateUserById(request, userDetails.getId());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        authUserService.changePassword(userDetails.getId(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Change password successfully!");
    }
}
