package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_user")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth2_id")
    private String oauth2Id;

    @Column(name = "provider", length = 45)
    private String provider;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Lob
    @Column(name = "access_token")
    private String accessToken;

    @Lob
    @Column(name = "refresh_token")
    private String refreshToken;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("1")
    @Column(name = "status")
    private Integer status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "auth_user_role",
            joinColumns = @JoinColumn(name = "auth_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
