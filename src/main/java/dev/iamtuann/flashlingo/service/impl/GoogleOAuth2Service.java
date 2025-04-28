package dev.iamtuann.flashlingo.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.Role;
import dev.iamtuann.flashlingo.enums.ERole;
import dev.iamtuann.flashlingo.exception.APIException;
import dev.iamtuann.flashlingo.model.AuthUserResponse;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.RoleRepository;
import dev.iamtuann.flashlingo.security.JwtTokenProvider;
import dev.iamtuann.flashlingo.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GoogleOAuth2Service implements OAuth2Service {

    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleIdTokenVerifier verifier;
    private final Random random = new Random();

    @Value("${app.google.client-id}")
    private String clientId;
    @Value("${app.google.client-secret}")
    private String clientSecret;
    @Value("${app.google.redirect_uri}")
    private String redirectUri;

    public GoogleOAuth2Service(@Value("${app.google.client-id}") String clientId, AuthUserRepository authUserRepository, RoleRepository repository, JwtTokenProvider jwtTokenProvider) throws GeneralSecurityException, IOException {
        this.authUserRepository = authUserRepository;
        this.roleRepository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        this.verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public String getIdToken(String code) {
        if (code == null) throw new APIException(HttpStatus.UNAUTHORIZED, "Authorization code is not valid");
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    this.clientId,
                    this.clientSecret,
                    code,
                    this.redirectUri
            ).execute();
            return tokenResponse.getIdToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUser verifyIdToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if (idTokenObj == null) throw new APIException(HttpStatus.UNAUTHORIZED, "Id token is not valid");
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            AuthUser user = new AuthUser();
            user.setEmail(payload.getEmail());
            user.setFirstName((String) payload.get("given_name"));
            user.setLastName((String) payload.get("family_name"));
            user.setAvatarUrl((String) payload.get("picture"));
            return user;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public AuthUserResponse loginOAuth(String code) {
        String idToken = this.getIdToken(code);
        AuthUser authUser = this.verifyIdToken(idToken);
        if (!authUserRepository.existsByEmail(authUser.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is not registered");
        }
        authUser = updateUser(authUser);
        String token = jwtTokenProvider.generateToken(authUser);
        return new AuthUserResponse(authUser, token);
    }

    @Override
    @Transactional
    public AuthUserResponse registerOAuth(String code) {
        String idToken = this.getIdToken(code);
        AuthUser authUser = this.verifyIdToken(idToken);
        if (authUserRepository.existsByEmail(authUser.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email account has been registered before, please login!");
        }
        authUser = createUser(authUser);
        String token = jwtTokenProvider.generateToken(authUser);
        return new AuthUserResponse(authUser, token);
    }

    @Override
    @Transactional
    public AuthUser createUser(AuthUser authUser) {
        authUser.setProvider("Google");
        authUser.setUsername(generateUsername());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.USER.getName()));
        authUser.setRoles(roles);
        authUser = authUserRepository.save(authUser);
        return authUser;
    }

    @Override
    @Transactional
    public AuthUser updateUser(AuthUser authUser) {
        AuthUser existUser = authUserRepository.findAuthUserByEmail((authUser.getEmail()));

        if (existUser.getProvider().equals("System")) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is already used for an account on the system");
        }
        existUser.setFirstName(authUser.getFirstName());
        existUser.setLastName(authUser.getLastName());
        existUser.setAvatarUrl(authUser.getAvatarUrl());
        return authUserRepository.save(existUser);
    }

    public String generateUsername() {
        String username;
        do {
            username = "User" + String.format("%08d", random.nextInt(100_000_000));
        } while (authUserRepository.existsByUsername(username));
        return username;
    }
}
