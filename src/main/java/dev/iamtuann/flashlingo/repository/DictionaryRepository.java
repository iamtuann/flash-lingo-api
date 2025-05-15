package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "dictionaryRepository")
public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {

    @Query(value = "SELECT d from Dictionary d " +
            "WHERE (d.word LIKE CONCAT(:prefix, '%')) " +
            "ORDER BY d.word ASC " +
            "LIMIT :limit")
    List<Dictionary> getSuggestWords(String prefix, Integer limit);

    @Query(value = "SELECT d.definition from Dictionary d " +
            "WHERE (d.word = :word) " +
            "ORDER BY d.word ASC " +
            "LIMIT 1")
    String getDefinition(String word);

    @Query(value = "SELECT DISTINCT d.pronunciation from Dictionary d " +
            "WHERE (d.word = :word) ")
    String getPronunciation(String word);

    Optional<Dictionary> findByWord(String word);
}
