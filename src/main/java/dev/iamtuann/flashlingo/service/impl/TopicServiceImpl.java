package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.enums.EStatusMode;
import dev.iamtuann.flashlingo.exception.NoPermissionException;
import dev.iamtuann.flashlingo.exception.ResourceNotFoundException;
import dev.iamtuann.flashlingo.mapper.TopicMapper;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.TopicService;
import dev.iamtuann.flashlingo.utils.CheckPermission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final AuthUserRepository authUserRepository;
    private final CheckPermission checkPermission;

    @Override
    public TopicDto findTopicById(Long id, Long authUserId) {
        if (checkPermission.viewableTopic(id, authUserId)) {
            Topic topic = topicRepository.findTopicById(id);
            return TopicMapper.INSTANCE.toDto(topic);
        } else {
            throw new NoPermissionException("access this resource");
        }
    }

    @Override
    public Set<TopicDto> findAllTopicsCreated(Long userId) {
        Set<Topic> topics = topicRepository.findAllByCreatedById(userId);
        return topics.stream().map(TopicMapper.INSTANCE::toDtoWithoutTerms).collect(Collectors.toSet());
    }

    @Override
    public Set<TopicDto> findAllPublicTopicsByUserId(Long userId) {
        Set<Topic> topics = topicRepository.findAllByCreatedByIdAndStatus(userId, EStatusMode.PUBLIC.getValue());
        return topics.stream().map(TopicMapper.INSTANCE::toDtoWithoutTerms).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public TopicDto save(TopicRequest request, Long userId) {
        if (!checkPermission.editableTopic(request.getId(), userId)) {
            throw new NoPermissionException("edit this topic");
        }
        Topic topic = new Topic();
        if (request.getId() == null) {
            topic.setCreatedBy(authUserRepository.findAuthUserById(userId));
            topic.setStatus(EStatusMode.DRAFT.getValue());
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
