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
    @NotBlank(message = "Term is required")
    private String term;
    @NotBlank(message = "Definition is required")
    private String definition;
    private Integer rank;
}
