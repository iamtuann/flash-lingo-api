package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.TopicLearning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "topicLearningRepository")
public interface TopicLearningRepository extends JpaRepository<TopicLearning, Long> {
    TopicLearning findByAuthUserIdAndTopicId(Long userId, Long topicId);
}
