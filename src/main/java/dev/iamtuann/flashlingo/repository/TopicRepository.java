package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "topicRepository")
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findTopicById(long id);

    @Query(value = "SELECT t FROM Topic t " +
            "LEFT JOIN t.folders f " +
            "WHERE (:name IS NULL OR :name = '' OR (t.name LIKE CONCAT('%', :name, '%'))) " +
            "AND (:folderId IS NULL OR (:folderId = f.id)) " +
            "AND (:userId IS NULL OR t.createdBy.id = :userId) " +
            "AND (:status IS NULL OR t.status = :status) ")
    Page<Topic> searchTopics(@Param("name") String name,
                             @Param("folderId") Long folderId,
                             @Param("userId") Long userId,
                             @Param("status") Integer status,
                             Pageable pageable);

    Optional<Topic> findByIdAndCreatedById(long id, long userId);

    boolean existsByIdAndCreatedById(long id, long userId);
}
