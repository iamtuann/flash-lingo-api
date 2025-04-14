package dev.iamtuann.flashlingo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthUserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private Date createdAt;
    private Date updatedAt;
    private Integer status;
    private Integer topicsNumber;
}
