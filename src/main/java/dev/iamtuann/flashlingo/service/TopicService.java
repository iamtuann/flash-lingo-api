package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicService {
    TopicDto findTopicById(Long id, Long authUserId);

    PageDto<TopicDto> searchTopics(String name, Long authId, Pageable pageable);

    PageDto<TopicDto> searchTopicsInFolder(String name, long folderId, Long authId, Pageable pageable);

    PageDto<TopicDto> searchTopicsUser(String name, Long userId, Long authId, Pageable pageable);

    TopicDto save(TopicRequest request, Long userId);

    TopicDto changeStatus(Long id, Integer status, Long userId);

    void increaseLearned(Long id);

    void deleteTopic(Long id, Long authUserId);

    void addTopicToFolders(Long topicId, List<Long> folderIds, long userId);
}
