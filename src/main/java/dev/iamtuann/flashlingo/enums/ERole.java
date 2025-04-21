package dev.iamtuann.flashlingo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ERole {
    ADMIN(1, "ADMIN"),
    TEACHER(3, "TEACHER"),
    USER(2, "USER");

    private final long id;
    private final String name;
}
