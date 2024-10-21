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
    private String token;

    public AuthUserResponse(UserDetailsImpl userDetails, String token) {
        this.id = userDetails.getId();
        this.email = userDetails.getUsername();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.token = token;
    }
}