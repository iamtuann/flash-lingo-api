package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {
    TopicDto findTopicById(Long id, Long authUserId);

    Page<TopicDto> searchTopics(String name, Long folderId, Long userId, Long authId, Pageable pageable);

    TopicDto save(TopicRequest request, Long userId);

    TopicDto changeStatus(Long id, Integer status, Long userId);
}
