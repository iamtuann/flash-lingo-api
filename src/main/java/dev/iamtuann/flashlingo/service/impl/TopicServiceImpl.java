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

    @Override
    public TopicDto findTopicById(Long id, Long authUserId) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (topicOptional.isEmpty()) {
            throw new ResourceNotFoundException("Topic", "id", id);
        }
        Topic topic = topicOptional.get();
        if (topic.getStatus().equals(EStatusMode.PUBLIC.getValue()) || topic.getCreatedBy().getId().equals(authUserId)) {
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
        Topic topic = new Topic();
        if (request.getId() == null) {
            topic.setCreatedBy(authUserRepository.findAuthUserById(userId));
            topic.setStatus(EStatusMode.DRAFT.getValue());
        } else {
            topic = topicRepository.findByIdAndCreatedById(request.getId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", request.getId()));
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
