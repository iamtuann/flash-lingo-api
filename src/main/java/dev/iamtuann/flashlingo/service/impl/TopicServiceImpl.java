package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.enums.EStatus;
import dev.iamtuann.flashlingo.exception.NoPermissionException;
import dev.iamtuann.flashlingo.exception.ResourceNotFoundException;
import dev.iamtuann.flashlingo.mapper.TopicMapper;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.FolderRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.TopicService;
import dev.iamtuann.flashlingo.utils.CheckPermission;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final FolderRepository folderRepository;
    private final AuthUserRepository authUserRepository;
    private final CheckPermission checkPermission;
    private final TopicMapper topicMapper = TopicMapper.INSTANCE;

    @Override
    @Cacheable(value = "topics", key = "#id")
    public TopicDto findTopicById(Long id, Long authUserId) {
        if (checkPermission.viewableTopic(id, authUserId)) {
            Topic topic = topicRepository.findTopicById(id);
            return topicMapper.toDtoWithoutTerms(topic);
        } else {
            throw new NoPermissionException("access this resource");
        }
    }

    @Override
    @Cacheable(value = "topics")
    public PageDto<TopicDto> searchTopics(String name, Long authId, Pageable pageable) {
        Page<Topic> topicPage = topicRepository.searchTopics(name, authId, pageable);
        List<TopicDto> topics =topicPage.stream()
                .map(topicMapper::toDtoWithoutTerms).collect(Collectors.toList());
        return new PageDto<>(topics, topicPage);
    }

    @Override
    @Cacheable(value = "topics-folder")
    public PageDto<TopicDto> searchTopicsInFolder(String name, long folderId, Long authId, Pageable pageable) {
        if (!checkPermission.viewableFolder(folderId, authId)) {
            throw new NoPermissionException("access this folder");
        }
        Page<Topic> topicPage = topicRepository.searchTopicsInFolder(name, folderId, pageable);
        List<TopicDto> topics =topicPage.stream()
                .map(topicMapper::toDtoWithoutTerms).collect(Collectors.toList());
        return new PageDto<>(topics, topicPage);
    }

    @Override
    @Cacheable(value = "topics-user")
    public PageDto<TopicDto> searchTopicsUser(String name, Long userId, Long authId, Pageable pageable) {
        Page<Topic> topicPage;
        if (authId != null && authId.equals(userId)) {
            topicPage = topicRepository.searchTopicsUser(name, userId, null, pageable);
        } else {
            topicPage = topicRepository.searchTopicsUser(name, userId, EStatus.PUBLIC.getValue(), pageable);
        }
        List<TopicDto> topics =topicPage.stream()
                .map(topicMapper::toDtoWithoutTerms).collect(Collectors.toList());
        return new PageDto<>(topics, topicPage);
    }


    @Override
    @Transactional
    @Caching(
            put = {
                    @CachePut(value = "topics", key = "#result.id")
            },
            evict = {
                    @CacheEvict(value = "topics", allEntries = true)
            }
    )
    public TopicDto save(TopicRequest request, Long userId) {
        if (!checkPermission.editableTopic(request.getId(), userId)) {
            throw new NoPermissionException("edit this topic");
        }
        Topic topic = new Topic();
        if (request.getId() == null) {
            topic.setCreatedBy(authUserRepository.findAuthUserById(userId));
            topic.setStatus(EStatus.DRAFT.getValue());
            if (request.getName() == null || request.getName().isBlank()) topic.setName("New topic");
            topic.setTerms(new HashSet<>());
        } else {
            topic = topicRepository.findTopicById(request.getId());
        }
        TopicMapper.INSTANCE.updateTopicFromRequest(request, topic);
        topic = topicRepository.save(topic);
        return TopicMapper.INSTANCE.toDto(topic);
    }

    @Override
    @Transactional
    @Caching(
            put = {
                    @CachePut(value = "topics", key = "#id")
            },
            evict = {
                    @CacheEvict(value = "topics", allEntries = true)
            }
    )
    public TopicDto changeStatus(Long id, Integer status, Long userId) {
        Optional<Topic> topicOptional = topicRepository.findByIdAndCreatedById(id, userId);
        if (topicOptional.isEmpty()) {
            throw new ResourceNotFoundException("Topic", "id", id);
        }
        Topic topic = topicOptional.get();
        topic.setStatus(status);
        topic = topicRepository.save(topic);
        return TopicMapper.INSTANCE.toDto(topic);
    }

    @Override
    public void increaseLearned(Long id) {
        Topic topic = topicRepository.findTopicById(id);
        topic.setLearnCount(topic.getLearnCount() + 1);
        topicRepository.save(topic);
    }

    @Override
    @Transactional
    @CacheEvict(value = "topics", allEntries = true)
    public void deleteTopic(Long id, Long authUserId) {
        if (!checkPermission.editableTopic(id, authUserId)) {
            throw new NoPermissionException("delete this topic");
        }
        topicRepository.deleteById(id);
    }

    @Override
    public void addTopicToFolders(Long topicId, List<Long> folderIds, long userId) {
        if(!checkPermission.viewableTopic(topicId, userId)) {
            throw new NoPermissionException("access this topic");
        }
        Topic topic = topicRepository.findTopicById(topicId);
        for (Long folderId : folderIds) {
            if(checkPermission.editableFolder(folderId, userId)) {
                Folder folder = folderRepository.findFolderById(folderId);
                folder.getTopics().add(topic);
                folder.setUpdatedAt(new Date());
                folderRepository.save(folder);
            }
        }
    }
}
