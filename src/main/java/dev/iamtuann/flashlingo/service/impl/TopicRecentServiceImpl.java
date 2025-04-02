package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.entity.TopicRecent;
import dev.iamtuann.flashlingo.mapper.TopicMapper;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.TopicRecentRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.TopicRecentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicRecentServiceImpl implements TopicRecentService {
    private final TopicRecentRepository topicRecentRepository;
    private final AuthUserRepository authUserRepository;
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper = TopicMapper.INSTANCE;

    @Override
    public void saveRecentTopic(Topic topic, AuthUser authUser) {
        if (authUser == null) return;
        Optional<TopicRecent> topicRecent = topicRecentRepository.findByTopicIdAndUserId(topic.getId(), authUser.getId());
        if (topicRecent.isPresent()) {
            topicRecent.get().setDate(new Date());
            topicRecentRepository.save(topicRecent.get());
        } else {
            TopicRecent newTopicRecent = new TopicRecent();
            newTopicRecent.setUser(authUser);
            newTopicRecent.setTopic(topic);
            newTopicRecent.setDate(new Date());
            topicRecentRepository.save(newTopicRecent);
        }
    }

    @Override
    public void saveRecentTopic(Long topicId, Long userId) {
        Topic topic = topicRepository.findTopicById(topicId);
        AuthUser authUser = authUserRepository.findAuthUserById(userId);
        this.saveRecentTopic(topic, authUser);
    }

    @Override
    public PageDto<TopicDto> searchRecentTopics(String name, Long authId, Pageable pageable) {
        Page<Topic> topicPage = topicRecentRepository.searchRecentTopics(name, authId, pageable);
        List<TopicDto> topics =topicPage.stream()
                .map(topicMapper::toDtoWithoutTerms).collect(Collectors.toList());
        return new PageDto<>(topics, topicPage);
    }
}
