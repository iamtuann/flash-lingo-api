package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.TopicFlashcardDto;

public interface TopicFlashcardService {
    TopicFlashcardDto findByUserIdAndTopicId(Long userId, Long topicId);

    void save(Long userId, TopicFlashcardDto dto);
}
