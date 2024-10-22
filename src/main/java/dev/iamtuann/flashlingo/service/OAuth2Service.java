package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.model.AuthUserResponse;

public interface OAuth2Service {
    AuthUser verifyIdToken(String idToken);

    AuthUserResponse loginOAuth(String idToken);

    AuthUser createOrUpdateUser(AuthUser authUser);
}
