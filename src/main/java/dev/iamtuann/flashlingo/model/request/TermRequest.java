package dev.iamtuann.flashlingo.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TermRequest {
    private Long id;
    private Long topicId;
    private String term;
    private String definition;
    private String pronunciation;
    private String partOfSpeech;
    private String example;
    private String level;
    private Integer rank;
    private String imageUrl;
}
