package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.entity.TopicRecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "topicRecentRepository")
public interface TopicRecentRepository extends JpaRepository<TopicRecent, Long> {

    Optional<TopicRecent> findByTopicIdAndUserId(Long topicId, Long userId);

    @Query(value = "SELECT t FROM TopicRecent tr " +
            "JOIN Topic t ON tr.topic.id = t.id " +
            "WHERE tr.user.id = :userId " +
            "AND (:name IS NULL OR :name = '' OR (tr.topic.name LIKE CONCAT('%', :name, '%'))) " +
            "ORDER BY tr.date DESC")
    Page<Topic> searchRecentTopics(@Param("name") String name,
                             @Param("userId") Long userId,
                             Pageable pageable);
}
