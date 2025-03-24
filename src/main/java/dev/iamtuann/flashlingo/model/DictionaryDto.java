package dev.iamtuann.flashlingo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictionaryDto {
    private Integer id;
    private String word;
    private String pronunciation;
    private String definition;
}
