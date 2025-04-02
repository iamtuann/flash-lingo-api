package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import org.springframework.data.domain.Pageable;

public interface TopicRecentService {
    void saveRecentTopic(Topic topic, AuthUser authUser);

    void saveRecentTopic(Long topicId, Long userId);

    PageDto<TopicDto> searchRecentTopics(String name, Long authId, Pageable pageable);
}
