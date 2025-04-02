package dev.iamtuann.flashlingo.service.impl;

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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public PageDto<TopicDto> searchTopics(String name, Long folderId, Long userId, Long authId, Pageable pageable) {
//        Integer status = EStatus.PUBLIC.getValue();
        if (folderId != null && !checkPermission.viewableFolder(folderId, authId)) {
            throw new NoPermissionException("access this folder");
        }
        Page<Topic> topicPage;
        if (authId != null && authId.equals(userId)) {
            topicPage = topicRepository.searchTopics(name, folderId, userId, null, pageable);
        } else {
            topicPage = topicRepository.searchTopics(name, folderId, userId, EStatus.PUBLIC.getValue(), pageable);
        }
        List<TopicDto> topics =topicPage.stream()
                .map(topicMapper::toDtoWithoutTerms).collect(Collectors.toList());
        return new PageDto<>(topics, topicPage);
    }

    @Override
    public PageDto<TopicDto> searchTopicsInFolder(String name, long folderId, Long authId, Pageable pageable) {
        if (!checkPermission.viewableFolder(folderId, authId)) {
            throw new NoPermissionException("access this folder");
        }
//        Folder folder = folderRepository
//        if (authId != null && authId.equals(userId)) {
//            topicPage = topicRepository.searchTopics(name, folderId, userId, null, pageable);
//        } else {
//            topicPage = topicRepository.searchTopics(name, folderId, userId, EStatus.PUBLIC.getValue(), pageable);
//        }
//        Page<Topic> topicPage;
        return null;
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
}
