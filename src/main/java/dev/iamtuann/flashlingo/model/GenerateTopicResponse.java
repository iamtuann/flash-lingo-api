package dev.iamtuann.flashlingo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenerateTopicResponse {
    private String name;
    private String description;
    private List<TermDto> terms;
    private String shortPassage;
}
