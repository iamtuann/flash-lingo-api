package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.TopicLearningDto;

public interface TopicLearningService {
    TopicLearningDto findByUserIdAndTopicId(Long userId, Long topicId);

    void save(Long userId, TopicLearningDto dto);
}
