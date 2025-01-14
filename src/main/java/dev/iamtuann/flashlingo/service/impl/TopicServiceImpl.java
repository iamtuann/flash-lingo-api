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
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final AuthUserRepository authUserRepository;
    private final CheckPermission checkPermission;
    private final TopicMapper topicMapper = TopicMapper.INSTANCE;
    private final FolderRepository folderRepository;

    @Override
    @Cacheable(value = "topics", key = "#id")
    public TopicDto findTopicById(Long id, Long authUserId) {
        if (checkPermission.viewableTopic(id, authUserId)) {
            Topic topic = topicRepository.findTopicById(id);
            return topicMapper.toDto(topic);
        } else {
            throw new NoPermissionException("access this resource");
        }
    }

    @Override
    @Cacheable(value = "topics")
    public PageDto<TopicDto> searchTopics(String name, Long folderId, Long userId, Long authId, Pageable pageable) {
        Integer status = EStatus.PUBLIC.getValue();
        if (folderId != null) {
            if (checkPermission.viewableFolder(folderId, authId)) {
                Folder folder = folderRepository.findFolderById(folderId);
                userId = folder.getCreatedBy().getId();
            } else {
                throw new NoPermissionException("access this folder");
            }
        }
        if (authId != null && Objects.equals(userId, authId)) {
            status = null;
        }
        Page<Topic> topicPage = topicRepository.searchTopics(name, status, folderId, userId, pageable);
        List<TopicDto> topics =topicPage.stream()
                .map(topicMapper::toDtoWithoutTerms).collect(Collectors.toList());
        return new PageDto<>(topics, topicPage);
    }


    @Override
    @Transactional
    @CachePut(value = "topics", key = "#request.id")
    public TopicDto save(TopicRequest request, Long userId) {
        if (!checkPermission.editableTopic(request.getId(), userId)) {
            throw new NoPermissionException("edit this topic");
        }
        Topic topic = new Topic();
        if (request.getId() == null) {
            topic.setCreatedBy(authUserRepository.findAuthUserById(userId));
            topic.setStatus(EStatus.DRAFT.getValue());
        } else {
            topic = topicRepository.findTopicById(request.getId());
        }
        TopicMapper.INSTANCE.updateTopicFromRequest(request, topic);
        topic = topicRepository.save(topic);
        return TopicMapper.INSTANCE.toDto(topic);
    }

    @Override
    @Transactional
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
}
