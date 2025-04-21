package dev.iamtuann.flashlingo.model;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String token;
    private String username;

    public AuthUserResponse(UserDetailsImpl userDetails, String token) {
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.email = userDetails.getEmail();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.avatarUrl = userDetails.getAvatarUrl();
        this.token = token;
    }

    public AuthUserResponse(AuthUser user, String token) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.avatarUrl = user.getAvatarUrl();
        this.token = token;
    }
}
