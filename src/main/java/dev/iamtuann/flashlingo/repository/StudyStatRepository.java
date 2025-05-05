package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.StudyStat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyStatRepository extends JpaRepository<StudyStat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StudyStat> findByAuthUserIdAndStatDate(Long userId, LocalDate date);

    List<StudyStat> findByAuthUserIdAndStatDateBetween(Long userId, LocalDate start, LocalDate end);
}
