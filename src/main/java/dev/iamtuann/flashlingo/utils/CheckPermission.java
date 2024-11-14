package dev.iamtuann.flashlingo.utils;

import dev.iamtuann.flashlingo.exception.ResourceNotFoundException;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CheckPermission {
    private final TopicRepository topicRepository;

    public boolean editableTopic(Long topicId, Long userId) {
        if (topicId == null) throw new IllegalArgumentException("topicId is not null");
        if (userId == null) return false;
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic", "id", topicId);
        }
        return topicRepository.existsByIdAndCreatedById(topicId, userId);
    }
}
