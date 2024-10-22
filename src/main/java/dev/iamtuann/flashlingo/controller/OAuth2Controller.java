package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.model.request.IdTokenDto;
import dev.iamtuann.flashlingo.service.impl.GoogleOAuth2Service;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    private final GoogleOAuth2Service googleOAuth2Service;

    @PostMapping(value = {"google-login", "google-signin"})
    public ResponseEntity<AuthUserResponse> login(@RequestBody IdTokenDto request) {
        AuthUserResponse userResponse = googleOAuth2Service.loginOAuth(request.getIdToken());
        return ResponseEntity.ok(userResponse);
    }
}
