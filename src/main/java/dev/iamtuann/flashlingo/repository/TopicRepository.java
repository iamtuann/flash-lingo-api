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
            "WHERE ( " +
            "(t.status = 1 AND " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :name, '%')) ) " +
            ") OR " +
            "( t.createdBy.id = :authId AND " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :name, '%')) ) " +
            ") )" )
    Page<Topic> searchTopics(@Param("name") String name,
                             @Param("authId") Long authId,
                             Pageable pageable);

    @Query(value = "SELECT t FROM Topic t " +
            "LEFT JOIN t.folders f " +
            "WHERE (:name IS NULL OR :name = '' OR (t.name LIKE CONCAT('%', :name, '%'))) " +
            "AND (:folderId IS NULL OR (:folderId = f.id)) ")
    Page<Topic> searchTopicsInFolder(@Param("name") String name,
                                     @Param("folderId") Long folderId,
                                     Pageable pageable);

    @Query(value = "SELECT t FROM Topic t " +
            "WHERE ( " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND t.createdBy.id = :userId " +
            "AND (:status IS NULL OR (:status = t.status)) )")
    Page<Topic> searchTopicsUser(@Param("name") String name,
                                 @Param("userId") Long userId,
                                 @Param("status") Integer status,
                                 Pageable pageable);

    Optional<Topic> findByIdAndCreatedById(long id, long userId);

    boolean existsByIdAndCreatedById(long id, long userId);
}
