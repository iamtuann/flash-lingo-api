package dev.iamtuann.flashlingo.enums;

import lombok.Getter;

@Getter
public enum EStatus {
    PUBLIC(1, "Public"),
    PRIVATE(0, "Private"),
    DRAFT(2, "Draft");

    private final Integer value;
    private final String title;

    EStatus(Integer value, String title) {
        this.value = value;
        this.title = title;
    }
}
