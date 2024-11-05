package dev.iamtuann.flashlingo.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicRequest {
    private Long id;
    private String name;
    private String description;
    private String termLang;
    private String defLang;
}
