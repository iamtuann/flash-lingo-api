package dev.iamtuann.flashlingo.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.iamtuann.flashlingo.entity.TopicLearning;
import dev.iamtuann.flashlingo.model.TopicLearningDto;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.TopicLearningRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.TopicLearningService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class TopicLearningServiceImpl implements TopicLearningService {
    private final TopicLearningRepository topicLearningRepository;
    private final TopicRepository topicRepository;
    private final AuthUserRepository authUserRepository;
    private final Gson gson;

    @Override
    public TopicLearningDto findByUserIdAndTopicId(Long userId, Long topicId) {
        TopicLearning topicLearning = topicLearningRepository.findByAuthUserIdAndTopicId(userId, topicId);
        if (topicLearning == null) {
            return null;
        } else {
            Type termIdsType = new TypeToken<List<Long>>() {}.getType();
            List<Long> termIds = gson.fromJson(topicLearning.getTermsLearning(), termIdsType);
            return new TopicLearningDto(topicId, termIds);
        }
    }

    @Override
    public void save(Long userId, TopicLearningDto dto) {
        TopicLearning topicLearning = topicLearningRepository.findByAuthUserIdAndTopicId(userId, dto.getTopicId());
        boolean hasTerms = dto.getTermLearningIds() != null && !dto.getTermLearningIds().isEmpty();
        if (topicLearning != null) {
            handleUpdateTopicLearning(topicLearning, dto, hasTerms);
        } else if (hasTerms) {
            topicLearning = new TopicLearning();
            topicLearning.setTopic(topicRepository.findTopicById(dto.getTopicId()));
            topicLearning.setAuthUser(authUserRepository.findAuthUserById(userId));
            topicLearning.setTermsLearning(gson.toJson(dto.getTermLearningIds()));
            topicLearningRepository.save(topicLearning);
        }
    }

    public void handleUpdateTopicLearning(TopicLearning topicLearning, TopicLearningDto dto, boolean hasTerms) {
        if (hasTerms) {
            topicLearning.setTermsLearning(gson.toJson(dto.getTermLearningIds()));
            topicLearningRepository.save(topicLearning);
        } else {
            topicLearningRepository.delete(topicLearning);
        }
    }
}
