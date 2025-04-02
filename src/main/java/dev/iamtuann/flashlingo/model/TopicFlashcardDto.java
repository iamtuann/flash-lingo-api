package dev.iamtuann.flashlingo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicFlashcardDto {
    private Long topicId;
    private List<Long> termLearningIds;
}
