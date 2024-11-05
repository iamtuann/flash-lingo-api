package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository(value = "topicRepository")
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Set<Topic> findAllByCreatedById(long authUserId);

    Set<Topic> findAllByCreatedByIdAndStatus(long userId, int status);

    Optional<Topic> findByIdAndCreatedById(long id, long userId);
}
