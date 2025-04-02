package dev.iamtuann.flashlingo.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.iamtuann.flashlingo.entity.TopicFlashcard;
import dev.iamtuann.flashlingo.model.TopicFlashcardDto;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.TopicFlashcardRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.TopicFlashcardService;
import dev.iamtuann.flashlingo.service.TopicRecentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class TopicFlashcardServiceImpl implements TopicFlashcardService {
    private final TopicFlashcardRepository topicFlashcardRepository;
    private final TopicRepository topicRepository;
    private final AuthUserRepository authUserRepository;
    private final Gson gson;

    @Override
    public TopicFlashcardDto findByUserIdAndTopicId(Long userId, Long topicId) {
        TopicFlashcard topicFlashcard = topicFlashcardRepository.findByAuthUserIdAndTopicId(userId, topicId);
        if (topicFlashcard == null) {
            return null;
        } else {
            Type termIdsType = new TypeToken<List<Long>>() {}.getType();
            List<Long> termIds = gson.fromJson(topicFlashcard.getTermsLearning(), termIdsType);
            return new TopicFlashcardDto(topicId, termIds);
        }
    }

    @Override
    public void save(Long userId, TopicFlashcardDto dto) {
        TopicFlashcard topicFlashcard = topicFlashcardRepository.findByAuthUserIdAndTopicId(userId, dto.getTopicId());
        boolean hasTerms = dto.getTermLearningIds() != null && !dto.getTermLearningIds().isEmpty();
        if (topicFlashcard != null) {
            handleUpdatetopicFlashcard(topicFlashcard, dto, hasTerms);
        } else if (hasTerms) {
            topicFlashcard = new TopicFlashcard();
            topicFlashcard.setTopic(topicRepository.findTopicById(dto.getTopicId()));
            topicFlashcard.setAuthUser(authUserRepository.findAuthUserById(userId));
            topicFlashcard.setTermsLearning(gson.toJson(dto.getTermLearningIds()));
            topicFlashcardRepository.save(topicFlashcard);
        }
    }

    public void handleUpdatetopicFlashcard(TopicFlashcard topicFlashcard, TopicFlashcardDto dto, boolean hasTerms) {
        if (hasTerms) {
            topicFlashcard.setTermsLearning(gson.toJson(dto.getTermLearningIds()));
            topicFlashcardRepository.save(topicFlashcard);
        } else {
            topicFlashcardRepository.delete(topicFlashcard);
        }
    }
}
