package dev.iamtuann.flashlingo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class AuthUserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String username;
    private LocalDate dob;
    private String bio;
    private String provider;
    private Date createdAt;
    private Date updatedAt;
    private Integer status;
    private Integer topicsNumber;
}
