package dev.iamtuann.flashlingo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ERole {
    ADMIN(1, "ADMIN"),
    STAFF(2, "STAFF"),
    USER(3, "USER");

    private final long id;
    private final String name;
}
