package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;

import java.util.Set;

public interface TopicService {
    TopicDto findTopicById(Long id, Long authUserId);

    Set<TopicDto> findAllTopicsCreated(Long userId);

    Set<TopicDto> findAllPublicTopicsByUserId(Long userId);

    TopicDto save(TopicRequest request, Long userId);

    TopicDto changeStatus(Long id, Integer status, Long userId);
}
