package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.TopicFlashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "topicFlashcardRepository")
public interface TopicFlashcardRepository extends JpaRepository<TopicFlashcard, Long> {
    TopicFlashcard findByAuthUserIdAndTopicId(Long userId, Long topicId);
}
