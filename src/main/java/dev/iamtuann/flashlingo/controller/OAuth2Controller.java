package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.service.impl.GoogleOAuth2Service;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    private final GoogleOAuth2Service googleOAuth2Service;

    @PostMapping(value = {"google-login", "google-signin"})
    public ResponseEntity<AuthUserResponse> login(@RequestBody Map<String, String> request) {
        AuthUserResponse userResponse = googleOAuth2Service.loginOAuth(request.get("code"));
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping(value = {"google-register", "google-signup"})
    public ResponseEntity<AuthUserResponse> register(@RequestBody Map<String, String> request) {
        AuthUserResponse userResponse = googleOAuth2Service.registerOAuth(request.get("code"));
        return ResponseEntity.ok(userResponse);
    }
}
