package dev.iamtuann.flashlingo.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTopicRequest {
    private String name;
    private String description;
    private int termCount;
    private String level;
}
