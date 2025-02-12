package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "termRepository")
public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByIdAndTopicId(long id, long topicId);

    List<Term> findAllByTopicIdOrderByRankAsc(long topicId);

    // Decrease rank
    @Modifying
    @Query("UPDATE Term t SET t.rank = t.rank - 1 WHERE t.topic.id = :topicId AND t.rank > :oldRank AND t.rank <= :newRank")
    void decrementRanks(Long topicId, int oldRank, int newRank);

    // Increase rank
    @Modifying
    @Query("UPDATE Term t SET t.rank = t.rank + 1 WHERE t.topic.id = :topicId AND t.rank >= :newRank AND t.rank < :oldRank")
    void incrementRanks(Long topicId, int oldRank, int newRank);
}
