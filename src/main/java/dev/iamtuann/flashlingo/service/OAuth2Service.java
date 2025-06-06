package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.model.AuthUserResponse;

public interface OAuth2Service {
    String getIdToken(String code);

    AuthUser verifyIdToken(String idToken);

    AuthUserResponse loginOAuth(String code);

    AuthUserResponse registerOAuth(String code);

    AuthUser createUser(AuthUser authUser);

    AuthUser updateUser(AuthUser authUser);
}
